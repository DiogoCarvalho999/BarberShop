package com.barbeiro.barbershop.repository;

import com.barbeiro.barbershop.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {}
