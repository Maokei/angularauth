package se.maokei.jwtserver_spring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import se.maokei.jwtserver_spring.entity.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/api" )
public class Api {
    //private final UserRepository;
    //private final EventRepository;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> helloFromApi() {
        return Collections.singletonMap("response","Hello from api");
    }

    @GetMapping("/events")
    public Flux<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Cooking Meetup","Bring carrots", new Date()));
        events.add(new Event("Auto export","Data export party",new Date()));
        events.add(new Event("Exorcism","Exercise a demon seminar",new Date()));
        events.add(new Event("Snow sculpting","Sculpt snow statues",new Date()));
        return Flux.fromIterable(events);
    }

    @GetMapping("/special")
    public Flux<Event> getSpecial() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Cooking Meetup","Bring carrots", new Date()));
        events.add(new Event("Auto export","Data export party",new Date()));
        events.add(new Event("Exorcism","Exercise a demon seminar",new Date()));
        events.add(new Event("Snow sculpting","Sculpt snow statues",new Date()));
        return Flux.fromIterable(events);
    }

    @PostMapping("/register")
    public void register() {

    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public void login(@RequestParam() String email, @RequestParam() String password) {

    }
}
