package com.clinic.patient.service;

import com.clinic.exception.CpfAlreadyExistsException;
import com.clinic.patient.dto.PatientRegisterRequestDTO;
import com.clinic.patient.dto.PatientResponseDTO;
import com.clinic.patient.model.Patient;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.user.model.User;
import com.clinic.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;

    public PatientResponseDTO register(PatientRegisterRequestDTO dto) {
        validateCpf(dto.getCpf());
        User savedUser = userService.createPatientUser(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword()
        );
        Patient patient = Patient.builder()
                .user(savedUser)
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .build();
        Patient savedPatient = patientRepository.save(patient);
        return toResponse(savedPatient);
    }

    private PatientResponseDTO toResponse(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .name(patient.getUser().getName())
                .email(patient.getUser().getEmail())
                .cpf(patient.getCpf())
                .phone(patient.getPhone())
                .build();
    }

    private void validateCpf(String cpf) {
        if (patientRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException("CPF já existe.");
        }
    }

}
