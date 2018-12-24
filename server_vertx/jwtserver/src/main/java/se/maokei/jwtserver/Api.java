package se.maokei.jwtserver;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


/**
* @class Api
* @desc Class contains rest api routes
* */
public class Api {
  private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);
  Vertx vertx = null;
  JWTAuth jwt = null;

  public Api(Vertx vertx) {
    this.vertx = vertx;
    //JWTAuth.create(vertx, new JsonObject());
    JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("RS256")
        .setPublicKey("badger")
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

  }

  public void loginRoute(RoutingContext rc) {

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
      .putHeader("content-type", "appplication/json")
      .end(Json.encodePrettily(events));
  }

  public void getSpecial(RoutingContext rc) {

  }

  public Router getApiSubrouter() {
    Router apiRoute = Router.router(vertx);
    apiRoute.route("/*").handler(this::defaultRoute);
    apiRoute.post("/login").handler(this::defaultRoute);
    apiRoute.get("/events").handler(this::getEvents);
    apiRoute.get("/special").handler(this::getSpecial);
    return apiRoute;
  }
}
