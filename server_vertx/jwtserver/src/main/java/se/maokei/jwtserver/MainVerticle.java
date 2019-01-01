package se.maokei.jwtserver;

import io.vertx.core.*;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * MainVerticle
 * */
public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args){
    VertxOptions vertxOptions = new VertxOptions();
    vertxOptions.setClustered(true);

    Vertx.clusteredVertx(vertxOptions, res -> {
      if(res.succeeded()) {
        Vertx vertx  = res.result();
        ConfigRetriever cr = ConfigRetriever.create(vertx);
        cr.getConfig(config -> {
          if(config.succeeded()) {
            JsonObject conf = config.result();
            DeploymentOptions options = new DeploymentOptions().setConfig(conf);
            vertx.deployVerticle(new MainVerticle(), options);
          }
        });
      }
    });
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Router router = Router.router(vertx);
    Api api = new Api(vertx);
    router.mountSubRouter("/api/", api.getApiSubrouter(vertx));

    router.route().handler(StaticHandler.create().setCachingEnabled(false));
    vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true))
      .requestHandler(router).listen(config().getInteger("http.port"), asyncResult -> {
        if(asyncResult.succeeded()) {
          LOGGER.info("Http server is running on port: " + config().getInteger("http.port"));
        }else{
          LOGGER.error("Could not start http server", asyncResult.cause());
        }
    });
  }

  /**
   * Replaces token in string with new value.
   * @param input Incoming string to be modified.
   * @param token Token value.
   * @param newValue Replacement string value.
   * @return Augmented string.
   * */
  public String replaceString(String input, String token, String newValue) {
    String output = input;
    while(output.indexOf(token) != -1) {
      output = output.replace(token, newValue);
    }
    return output;
  }
}
