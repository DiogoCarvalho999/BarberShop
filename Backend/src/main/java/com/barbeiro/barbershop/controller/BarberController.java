package com.barbeiro.barbershop.controller;

import com.barbeiro.barbershop.model.Barber;
import com.barbeiro.barbershop.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberRepository barberRepository;

    @GetMapping
    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }
}
