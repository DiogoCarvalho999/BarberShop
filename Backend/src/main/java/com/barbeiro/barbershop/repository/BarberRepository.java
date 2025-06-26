package com.barbeiro.barbershop.repository;

import com.barbeiro.barbershop.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberRepository extends JpaRepository<Barber, Long> {}
