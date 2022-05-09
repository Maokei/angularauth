package se.maokei.jwtserver.handler;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.NoArgsConstructor;
import se.maokei.jwtserver.service.EventService;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

@NoArgsConstructor
public class EventHandler {
  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_JSON = "application/json";
  private EventService eventService;

  public EventHandler(EventService eventService) {
    this.eventService = eventService;
  }

  public Future<JsonObject> hello(RoutingContext rc) {
    JsonObject jo = new JsonObject();
    jo.put("Hello", "world");

    rc.response()
      .setStatusCode(200)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(jo));
    return Future.succeededFuture(jo);
  }
}
