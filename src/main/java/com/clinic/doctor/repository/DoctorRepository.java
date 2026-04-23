package com.clinic.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.doctor.model.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    boolean existsByCrmIgnoreCase(String crm);

    Optional<Doctor> findByUserId(UUID userId);

    List<Doctor> findByActiveTrue();

    List<Doctor> findByActiveTrueOrderByUserNameAsc();

    List<Doctor> findBySpecialtyIdAndActiveTrueOrderByUserNameAsc(UUID specialtyId);
}
