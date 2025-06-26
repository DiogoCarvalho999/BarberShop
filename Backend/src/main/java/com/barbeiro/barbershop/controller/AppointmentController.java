package com.barbeiro.barbershop.controller;

import com.barbeiro.barbershop.dto.AppointmentRequest;
import com.barbeiro.barbershop.model.Appointment;
import com.barbeiro.barbershop.model.Barber;
import com.barbeiro.barbershop.model.Service;
import com.barbeiro.barbershop.repository.AppointmentRepository;
import com.barbeiro.barbershop.repository.BarberRepository;
import com.barbeiro.barbershop.repository.ServiceRepository;
import com.barbeiro.barbershop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.barbeiro.barbershop.util.TimeSlotUtil;
import com.barbeiro.barbershop.service.AvailableSlotsService;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;
    private final AvailableSlotsService availableSlotsService;


    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        Barber barber = barberRepository.findById(request.getBarberId())
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Verificação de conflito
        if (appointmentRepository.findByBarberAndDateAndTime(barber, request.getDate(), request.getTime()).isPresent()) {
            return ResponseEntity.status(409).body("Este horário já está ocupado para este barbeiro.");
        }

        Appointment appointment = Appointment.builder()
                .customerName(request.getCustomerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .date(request.getDate())
                .time(request.getTime())
                .barber(barber)
                .service(service)
                .status("confirmed")
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // Enviar email de confirmação
        String subject = "Confirmação da sua marcação";
        String content = String.format(
                "Olá %s,\n\nA sua marcação foi confirmada para o dia %s às %s com o barbeiro %s para o serviço '%s'.\n\nObrigado!",
                request.getCustomerName(),
                request.getDate(),
                request.getTime(),
                barber.getName(),
                service.getName()
        );

        emailService.sendAppointmentConfirmation(request.getEmail(), subject, content);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/available")
    public List<LocalTime> getAvailableTimeSlots(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        // Usa o serviço com os horários válidos do dia da semana
        List<LocalTime> validSlots = availableSlotsService.getAvailableSlots(date);

        // Filtra os horários já ocupados para esse barbeiro
        List<LocalTime> bookedSlots = appointmentRepository.findByBarberIdAndDate(barberId, date)
                .stream()
                .map(Appointment::getTime)
                .toList();

        // Retorna apenas os horários válidos e livres
        return validSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .toList();
    }

}
