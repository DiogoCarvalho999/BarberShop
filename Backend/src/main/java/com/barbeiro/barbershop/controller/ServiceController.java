package com.barbeiro.barbershop.controller;

import com.barbeiro.barbershop.model.Service;
import com.barbeiro.barbershop.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}

