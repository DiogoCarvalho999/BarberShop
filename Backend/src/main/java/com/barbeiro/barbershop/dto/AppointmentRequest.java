package com.barbeiro.barbershop.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private String customerName;
    private String phone;
    private String email;
    private LocalDate date;
    private LocalTime time;
    private Long barberId;
    private Long serviceId;
}
