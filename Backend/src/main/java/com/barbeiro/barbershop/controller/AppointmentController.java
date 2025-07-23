package com.barbeiro.barbershop.controller;

import com.barbeiro.barbershop.dto.AppointmentRequest;
import com.barbeiro.barbershop.model.Appointment;
import com.barbeiro.barbershop.model.Barber;
import com.barbeiro.barbershop.model.Service;
import com.barbeiro.barbershop.repository.AppointmentRepository;
import com.barbeiro.barbershop.repository.BarberRepository;
import com.barbeiro.barbershop.repository.ServiceRepository;
import com.barbeiro.barbershop.service.AppointmentService;
import com.barbeiro.barbershop.service.EmailService;
import com.barbeiro.barbershop.service.AvailableSlotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;
    private final AvailableSlotsService availableSlotsService;
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        Barber barber = barberRepository.findById(request.getBarberId())
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Verifica se já existe uma marcação nesse horário
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

        Appointment savedAppointment = appointmentRepository.save(appointment);

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

        emailService.sendEmail(request.getEmail(), subject, content);

        return ResponseEntity.ok(savedAppointment);
    }

    @GetMapping("/available")
    public List<LocalTime> getAvailableTimeSlots(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<LocalTime> validSlots = availableSlotsService.getAvailableSlots(date);

        List<LocalTime> bookedSlots = appointmentRepository.findByBarberIdAndDate(barberId, date)
                .stream()
                .map(Appointment::getTime)
                .toList();

        return validSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .toList();
    }

    @GetMapping("/appointments/{barberId}/{date}")
    public ResponseEntity<List<String>> getOccupiedSlots(
            @PathVariable Long barberId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Appointment> appointments = appointmentRepository.findByBarberIdAndDate(barberId, date);

        List<String> occupiedTimes = appointments.stream()
                .map(app -> app.getTime().toString())
                .toList();

        return ResponseEntity.ok(occupiedTimes);
    }
}

