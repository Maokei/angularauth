package se.maokei.event_jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.maokei.event_jwt.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {
    private final List<Event> events = new ArrayList<>();
    private final List<Event> special = new ArrayList<>();

    public EventController() {
        events.add(new Event("Cooking Meetup","Bring carrots", LocalDate.now()));
        events.add(new Event("Auto export","Data export party", LocalDate.now()));
        events.add(new Event("Exorcism","Exercise a demon seminar", LocalDate.now()));
        events.add(new Event("Snow sculpting","Sculpt snow statues", LocalDate.now()));
        special.addAll(events);
        special.add(new Event("Super snow sculpting","Sculpt special statues out of snow", LocalDate.now()));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/special")
    public ResponseEntity<List<Event>> getSpecial() {
        return new ResponseEntity<>(special, HttpStatus.OK);
    }
}
