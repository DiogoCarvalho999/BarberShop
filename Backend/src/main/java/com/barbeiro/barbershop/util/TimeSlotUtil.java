package com.barbeiro.barbershop.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotUtil {
    public static List<LocalTime> getDefaultTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);   // Início às 09:00
        LocalTime end = LocalTime.of(18, 0);    // Fim às 18:00

        while (!start.isAfter(end)) {
            slots.add(start);
            start = start.plusMinutes(30); // Incremento de 30 minutos
        }

        return slots;
    }
}

