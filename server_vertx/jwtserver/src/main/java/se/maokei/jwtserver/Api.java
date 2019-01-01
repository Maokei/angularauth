package se.maokei.jwtserver;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.impl.JWTAuthHandlerImpl;
import se.maokei.jwtserver.entity.User;


/**
* @class Api
* @desc Class contains rest api routes
* */
public class Api {
  private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);
  Vertx vertx;
  JWTAuth jwt;

  public Api(Vertx vertx) {
    this.vertx = vertx;
    jwt = JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setPublicKey("keyboard badger")
        .setSymmetric(true)
        .setSecretKey("kitty")
      )
    );
  }

  public void defaultRoute(RoutingContext rc) {
    JsonObject jo = new JsonObject();
    jo.put("message", "FROM api route");
    rc.response().setStatusCode(200).putHeader("content-header", "application/json")
      .end(Json.encodePrettily(jo));
  }

  public void verifyToken(RoutingContext rc) {
    if(rc.request().headers().get("authToken").isEmpty()) {
      rc.response().setStatusCode(401).end("unauthorized request");
      /*JWTAuthHandlerImpl handler = (JWTAuthHandlerImpl) JWTAuthHandler.create(jwt);
      handler.parseCredentials(rc, handle -> {

      });*/
      return;
    }
    final String token = rc.request().headers().get("authToken");
    if(token == null) {
      rc.response().setStatusCode(401).end("unauthorized request");
      return;
    }

    rc.next();
  }

  public void loginRoute(RoutingContext rc) {
    JsonObject userData = rc.getBodyAsJson();
    JsonObject cmd = new JsonObject();
    cmd.put("cmd", "findUserByEmail");
    cmd.put("user", userData);
    vertx.eventBus().send("se.maokei.mongoservice", cmd.toString(), reply -> {
      if(reply.succeeded()) {
        JsonObject user = new JsonObject(reply.result().body().toString());
        if(!user.getString("password").equals(userData.getString("password"))) {
          rc.response().setStatusCode(401).end("!Invalid password");
        }else {
          String token = jwt.generateToken(new JsonObject().put("subject", user.getString("_id")), new JWTOptions());
          rc.response().setStatusCode(200).end(new JsonObject().put("token", token).toString());
        }
      }else{
        rc.response().setStatusCode(401).end("login bad");
      }
    });
  }

  public void registerRoute(RoutingContext rc) {
    int resCode = 400;
    JsonObject response = new JsonObject();
    JsonObject body = rc.getBodyAsJson();
    //Some validation
    if(body.containsKey("email") && body.getString("email").length() > 3 && body.containsKey("password") && body.getString("password").length() > 4 ) {
      JsonObject cmd = new JsonObject();
      cmd.put("cmd", "addUser");
      User user = body.mapTo(User.class);
      cmd.put("user", JsonObject.mapFrom(user));
      vertx.eventBus().send("se.maokei.mongoservice", cmd.toString(), reply -> {
        if(reply.succeeded()) {
          JsonObject res = new JsonObject(reply.result().body().toString());
          response.mergeIn(res);
        }else{
          response.put("error", "Some error");
        }
      });

    }else{
      response.put("error", "Bad user data");
    }
    rc.response().setStatusCode(resCode).end(Json.encodePrettily(response));
  }

  public void getEvents(RoutingContext rc) {
    final String data = "{\n" +
      "            \"_id\": \"1\",\n" +
      "            \"name\": \"Auto export\",\n" +
      "            \"description\": \"\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"2\",\n" +
      "            \"name\": \"Eet export\",\n" +
      "            \"description\": \"Stuff chicken\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"3\",\n" +
      "            \"name\": \"Walk the dog\",\n" +
      "            \"description\": \"Take the dog for a walk around the lake\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"4\",\n" +
      "            \"name\": \"Eutanice the dog\",\n" +
      "            \"description\": \"The dog is old take him out back behind the barn and let him have a taste of the shotgun while he's eating his favorite snack\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"5\",\n" +
      "            \"name\": \"repair car\",\n" +
      "            \"description\": \"Repair the broken engine in the volvo\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"6\",\n" +
      "            \"name\": \"Clean the house\",\n" +
      "            \"description\": \"Clean the house before the relative arrive!\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        }";
    JsonObject events = new JsonObject(data);
    rc.response().setStatusCode(200)
      .putHeader("content-type", "application/json")
      .end(Json.encodePrettily(events));
  }

  public void getSpecial(RoutingContext rc) {
    final String data = "{\n" +
      "            \"_id\": \"1\",\n" +
      "            \"name\": \"Auto export\",\n" +
      "            \"description\": \"\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"2\",\n" +
      "            \"name\": \"Eet export\",\n" +
      "            \"description\": \"Stuff chicken\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"3\",\n" +
      "            \"name\": \"Walk the dog\",\n" +
      "            \"description\": \"Take the dog for a walk around the lake\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"4\",\n" +
      "            \"name\": \"Eutanice the dog\",\n" +
      "            \"description\": \"The dog is old take him out back behind the barn and let him have a taste of the shotgun while he's eating his favorite snack\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"5\",\n" +
      "            \"name\": \"repair car\",\n" +
      "            \"description\": \"Repair the broken engine in the volvo\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        },\n" +
      "        {\n" +
      "            \"_id\": \"6\",\n" +
      "            \"name\": \"Clean the house\",\n" +
      "            \"description\": \"Clean the house before the relative arrive!\",\n" +
      "            \"date\": \"2018-01-01\"\n" +
      "        }";
    JsonObject events = new JsonObject(data);
    rc.response().setStatusCode(200)
      .putHeader("content-type", "appplication/json")
      .end(Json.encodePrettily(events));
  }

  public Router getApiSubrouter(Vertx vertx) {
    this.vertx = vertx;
    Router apiRoute = Router.router(vertx);
    apiRoute.route("/").handler(this::defaultRoute);
    apiRoute.route("/*").handler(BodyHandler.create());
    apiRoute.post("/login").handler(this::loginRoute);
    apiRoute.post("/register").handler(this::registerRoute);
    apiRoute.get("/events").handler(this::getEvents);
    apiRoute.route("/special*").handler(JWTAuthHandler.create(jwt));
    apiRoute.get("/special").handler(this::getSpecial);
    return apiRoute;
  }
}
