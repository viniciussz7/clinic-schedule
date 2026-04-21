package com.clinic.specialty.service;

import com.clinic.exception.SpecialtyAlreadyExistsException;
import com.clinic.specialty.dto.CreateSpecialtyRequestDTO;
import com.clinic.specialty.dto.SpecialtyResponseDTO;
import com.clinic.specialty.model.Specialty;
import com.clinic.specialty.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyResponseDTO create(CreateSpecialtyRequestDTO requestDTO) {
        String normalizedName = requestDTO.getName().trim();

        if (specialtyRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new SpecialtyAlreadyExistsException("Especialidade já existe.");
        }

        Specialty specialty = Specialty.builder()
                .name(normalizedName)
                .build();

        Specialty saved = specialtyRepository.save(specialty);

        return toResponse(saved);
    }

    public List<SpecialtyResponseDTO> listActive() {
        return specialtyRepository
                .findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SpecialtyResponseDTO toResponse(Specialty specialty) {
        return SpecialtyResponseDTO.builder()
                .id(specialty.getId().toString())
                .name(specialty.getName())
                .active(specialty.getActive())
                .build();
    }
}
