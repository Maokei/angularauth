package se.maokei.jwtserver.repository;

import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import se.maokei.jwtserver.model.Event;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventRepository.class);
  private List<Event> events;

  public EventRepository() {
    events = List.of(
      Event.builder()
        ._id(1)
        .name("Auto export")
        .description("No description")
        .date(new Date())
        .build(),
      Event.builder()
        ._id(2)
        .name("Java ee")
        .description("Going to Jakarta")
        .date(new Date())
        .build(),
      Event.builder()
        ._id(3)
        .name("Walk the dog")
        .description("Take the dog for a walk around the lake")
        .date(new Date())
        .build(),
      Event.builder()
        ._id(4)
        .name("Eutanice the dog")
        .description("The dog is old take him out back behind the barn and let him have a taste of the shotgun while he's eating his favorite snack")
        .date(new Date())
        .build(),
      Event.builder()
        ._id(5)
        .name("repair car")
        .description("Repair the broken engine in the volvo")
        .date(new Date())
        .build()
    );
  }

  /**
   * Read all events using pagination
   *
   * @param limit      Limit
   * @param offset     Offset
   * @return List<Event>
   */
  public Future<List<Event>> getAllEventsLimit(int limit, int offset) {
    List<Event> result =
      events.stream()
        .skip(offset)  // Equivalent to SQL's offset
        .limit(limit) // Equivalent to SQL's limit
        .collect(Collectors.toList());
    return Future.succeededFuture(result)
      .onSuccess(success -> LOGGER.info("All event was asked for!"))
      .onFailure(fail -> LOGGER.error("Unable to return all books"));
  }

  public Future<List<Event>> getAllEvents() {
    return Future.succeededFuture(events)
      .onSuccess(success -> LOGGER.info("All event was asked for!"))
      .onFailure(fail -> LOGGER.error("Unable to return all books"));
  }
}
