package se.maokei.jwtserver.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import se.maokei.jwtserver.handler.MediaPumpHandler;

public class MediaRouter {
  private final Vertx vertx;
  private final MediaPumpHandler mediaPumpHandler;

  public MediaRouter(Vertx vertx, MediaPumpHandler mediaPumpHandler) {
    this.vertx = vertx;
    this.mediaPumpHandler = mediaPumpHandler;
  }

  public void setRouter(Router router) {
    router.mountSubRouter("/api/v1/media", buildRouter());
  }

  private Router buildRouter() {
    final Router router = Router.router(vertx);
    router.get("/song").handler(mediaPumpHandler::getAudio);
    router.get("/video").handler(mediaPumpHandler::getVideo);
    return router;
  }
}
