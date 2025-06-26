package com.barbeiro.barbershop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phone;
    private String email;

    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    private Barber barber;

    @ManyToOne
    private Service service;

    private String status; // ex: "confirmed", "cancelled"
}
