package se.maokei.jwtserver_spring.repository;

import org.springframework.data.repository.CrudRepository;
import se.maokei.jwtserver_spring.entity.Event;

public interface EventRepository extends CrudRepository<Event, Long> {

}
