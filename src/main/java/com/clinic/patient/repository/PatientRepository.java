package com.clinic.patient.repository;

import com.clinic.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByCpf(String cpf);
    Optional<Patient> findByUserId(UUID userId);
}
