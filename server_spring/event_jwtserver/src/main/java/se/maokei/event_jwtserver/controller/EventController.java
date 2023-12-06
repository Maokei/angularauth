package se.maokei.event_jwtserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.maokei.event_jwtserver.service.EventService;

@RestController
@RequestMapping(value = "/api")
public class EventController implements EventControllerApi {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    @Override
    public ResponseEntity<?> events() {
        return null;
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/special")
    @Override
    public ResponseEntity<?> specialEvents() {
        return null;
    }
}
