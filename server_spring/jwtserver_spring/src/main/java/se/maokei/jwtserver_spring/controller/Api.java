package se.maokei.jwtserver_spring.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import se.maokei.jwtserver_spring.entity.Event;
import se.maokei.jwtserver_spring.entity.User;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/api" )
public class Api {
    //private final UserRepository;
    //private final EventRepository;

    @GetMapping("")
    public String helloFromApi() {
        return "Hello from api";
    }

    @GetMapping("/events")
    public Flux<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Meetup","", new Date()));
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        return Flux.fromIterable(events);
    }

    @PostMapping("/register")
    public void register() {

    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public void login(@RequestParam() String email, @RequestParam() String password) {

    }
}
