package se.maokei.jwtserver;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


/**
* @class Api
* @desc Class contains rest api routes
* */
public class Api {
  private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);
  Vertx vertx = null;

  public Api() {

  }

  public void defaultRoute(RoutingContext rc) {
    JsonObject jo = new JsonObject();
    jo.put("message", "FROM api route");
    rc.response().setStatusCode(200).putHeader("content-header", "application/json")
      .end(Json.encodePrettily(jo));
  }

  public void loginRoute(RoutingContext rc) {

  }

  public void getEvents(RoutingContext rc) {

  }

  public void getSpecial(RoutingContext rc) {

  }

  public Router getApiSubrouter(Vertx vertx) {
    Router apiRoute = Router.router(vertx);
    apiRoute.route("/*").handler(this::defaultRoute);
    apiRoute.post("/login").handler(this::defaultRoute);
    apiRoute.get("/events").handler(this::getEvents);
    apiRoute.get("/special").handler(this::getSpecial);

    return apiRoute;
  }
}
