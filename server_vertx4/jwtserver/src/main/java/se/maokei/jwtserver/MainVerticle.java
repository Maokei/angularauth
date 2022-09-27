package se.maokei.jwtserver;

import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.schema.VertxDataFetcher;
import se.maokei.jwtserver.handler.EventHandler;
import se.maokei.jwtserver.handler.MediaPumpHandler;
import se.maokei.jwtserver.model.Event;
import se.maokei.jwtserver.repository.EventRepository;
import se.maokei.jwtserver.router.EventRouter;
import se.maokei.jwtserver.router.MediaRouter;
import se.maokei.jwtserver.router.MetricsRouter;
import se.maokei.jwtserver.service.EventService;

import javax.print.attribute.standard.Media;

import java.util.List;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

public class MainVerticle extends AbstractVerticle {
  private EventService eventService;

public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new MainVerticle());
}
  private VertxDataFetcher<List<Event>> eventsDataFetcher() {
    return VertxDataFetcher.create((DataFetchingEnvironment dfe) -> eventService.allEvents());
  }

  private GraphQL setupGraphQL() {
    // Read the schema file from the file system.
    String schema = vertx.fileSystem().readFileBlocking("schema/schema.graphql").toString();
    //Parse  schema and create a TypeDefinitionRegistry
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    // RuntimeWiring linking our schema/TypeDefinitionRegistry to our services
    RuntimeWiring runtimeWiring = newRuntimeWiring()
      .type(newTypeWiring("Query")
        //.dataFetcher("eventById", eventByIdDataFetcher())
        .dataFetcher("getEvents", eventsDataFetcher()))
      .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    return GraphQL.newGraphQL(graphQLSchema).build();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    GraphQLHandlerOptions graphQLHandlerOptions = new GraphQLHandlerOptions()
      .setRequestBatchingEnabled(true);

    GraphQL graphQL = setupGraphQL();
    GraphQLHandler graphQLHandler = GraphQLHandler.create(graphQL, graphQLHandlerOptions);
    // GraphiQL setup
    GraphiQLHandlerOptions options = new GraphiQLHandlerOptions()
      .setEnabled(true);

    final EventRepository eventRepository = new EventRepository();
    eventService = new EventService(eventRepository);
    final EventHandler eventHandler = new EventHandler(eventService);
    final EventRouter eventRouter = new EventRouter(vertx, eventHandler);
    final MediaRouter mediaRouter = new MediaRouter(vertx, new MediaPumpHandler(vertx));

    final Router router = Router.router(vertx);
    eventRouter.setRouter(router);
    mediaRouter.setRouter(router);

    router.route().handler(BodyHandler.create());
    router.route("/static/*").handler(StaticHandler.create());
    router.route("/graphql").handler(graphQLHandler);
    router.route("/graphiql/*").handler(GraphiQLHandler.create(options));


    //ErrorHandler.buildHandler(router);
    //HealthCheckRouter.setRouter(vertx, router, dbClient);
    //MetricsRouter.setRouter(router);
    //eventRouter.setRouter(router);

    final int port = 8888;

    vertx.createHttpServer().requestHandler(router).listen(port, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
