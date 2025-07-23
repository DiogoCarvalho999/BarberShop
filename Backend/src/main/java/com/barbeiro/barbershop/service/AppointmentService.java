package com.barbeiro.barbershop.service;

import com.barbeiro.barbershop.model.Appointment;
import com.barbeiro.barbershop.model.Barber;
import com.barbeiro.barbershop.repository.AppointmentRepository;
import com.barbeiro.barbershop.repository.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BarberRepository barberRepository;

    public Appointment saveAppointment(Appointment appointment) {
        Optional<Barber> barberOpt = barberRepository.findById(appointment.getBarber().getId());
        if (barberOpt.isEmpty()) {
            throw new IllegalArgumentException("Barbeiro não encontrado com ID: " + appointment.getBarber().getId());
        }

        boolean isSlotTaken = appointmentRepository
                .findByBarberAndDateAndTime(barberOpt.get(), appointment.getDate(), appointment.getTime())
                .isPresent();

        if (isSlotTaken) {
            throw new IllegalStateException("Este horário já está ocupado para o barbeiro selecionado.");
        }

        appointment.setBarber(barberOpt.get());
        return appointmentRepository.save(appointment);
    }

    public List<LocalTime> getBookedTimesForBarber(Long barberId, LocalDate date) {
        return appointmentRepository.findByBarberIdAndDate(barberId, date)
                .stream()
                .map(Appointment::getTime)
                .toList();
    }
}
