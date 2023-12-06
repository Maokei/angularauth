package se.maokei.event_jwtserver.controller;

import org.springframework.http.ResponseEntity;

public interface EventControllerApi {
    public ResponseEntity<?> events();
    public ResponseEntity<?> specialEvents();
}
