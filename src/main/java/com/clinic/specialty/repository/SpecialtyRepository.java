package com.clinic.specialty.repository;

import com.clinic.specialty.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {

    boolean existsByNameIgnoreCase(String name);
    Optional<Specialty> findByNameIgnoreCase(String name);
    List<Specialty> findByActiveTrue();
    List<Specialty> findByActiveTrueOrderByNameAsc();
}
