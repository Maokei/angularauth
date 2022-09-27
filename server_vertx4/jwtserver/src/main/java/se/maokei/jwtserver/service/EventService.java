package se.maokei.jwtserver.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import se.maokei.jwtserver.model.Event;
import se.maokei.jwtserver.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

public class EventService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
  private final EventRepository eventRepository;

  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public Future<JsonObject> fetchAll() {
    Promise<JsonObject> promise = Promise.promise();
    eventRepository.getAllEvents().onSuccess(e -> {
      //promise.complete(JsonObject.mapFrom(e));
      promise.complete();
    }).onFailure(throwable -> {
      promise.fail(":(");
      LOGGER.error("EventService unable to get all Events");
    });
    return promise.future();
  }

  public Future<List<Event>> allEvents() {
    return this.eventRepository.getAllEvents();
  }
}
