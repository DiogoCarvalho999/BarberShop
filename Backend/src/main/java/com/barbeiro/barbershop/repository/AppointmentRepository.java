package com.barbeiro.barbershop.repository;

import com.barbeiro.barbershop.model.Appointment;
import com.barbeiro.barbershop.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByBarberAndDateAndTime(Barber barber, LocalDate date, LocalTime time);
    List<Appointment> findByBarberIdAndDate(Long barberId, LocalDate date);
}
