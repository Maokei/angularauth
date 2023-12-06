package se.maokei.event_jwtserver.dto;

import java.time.LocalDate;

public record Event(String name, String description, LocalDate date) {
}