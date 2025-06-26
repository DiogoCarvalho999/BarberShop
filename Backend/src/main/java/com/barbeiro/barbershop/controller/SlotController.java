package com.barbeiro.barbershop.controller;

import com.barbeiro.barbershop.service.AvailableSlotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final AvailableSlotsService slotsService;

    @GetMapping
    public List<LocalTime> getSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return slotsService.getAvailableSlots(date);
    }
}
