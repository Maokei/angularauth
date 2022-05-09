package se.maokei.jwtserver.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.AllArgsConstructor;
import se.maokei.jwtserver.handler.EventHandler;

@AllArgsConstructor
public class EventRouter {
  private Vertx vertx;
  private EventHandler eventHandler;

  public void setRouter(Router router) {
    router.mountSubRouter("/api/v1", buildEventRouter());
  }

  public Router buildEventRouter() {
    final Router eventRouter = Router.router(vertx);
    eventRouter.route("/event*").handler(BodyHandler.create());
    eventRouter.get("/event").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(eventHandler::hello);
    eventRouter.get("/hello").handler(eventHandler::hello);
    return eventRouter;
  }
}
