package com.barbeiro.barbershop.service;

import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailableSlotsService {

    public List<LocalTime> getAvailableSlots(LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();
        DayOfWeek day = date.getDayOfWeek();

        LocalTime start;
        LocalTime end;

        switch (day) {
            case MONDAY -> {
                start = LocalTime.of(9, 0);
                end = LocalTime.of(20, 0);
            }
            case TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY -> {
                start = LocalTime.of(9, 0);
                end = LocalTime.of(23, 0);
            }
            case SUNDAY -> {
                // Domingo fechado
                return slots;
            }
            default -> throw new IllegalStateException("Unexpected value: " + day);
        }

        LocalTime current = start;
        while (!current.isAfter(end.minusMinutes(30))) {
            slots.add(current);
            current = current.plusMinutes(30);
        }

        return slots;
    }
}
