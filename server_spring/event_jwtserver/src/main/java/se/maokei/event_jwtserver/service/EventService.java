package se.maokei.event_jwtserver.service;

import org.springframework.stereotype.Service;
import se.maokei.event_jwtserver.dto.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    public EventService() {
    }

    Optional<List<Event>> getEvents() {
        return Optional.of(List.of(new Event("Battle", "Slay evil demons", LocalDate.parse("23-12-2023"))));
    }

    Optional<List<Event>> getSpecial() {
        return Optional.of(List.of(new Event("Secret battle", "Slay evil demons again", LocalDate.parse("23-12-2023"))));
    }
}
