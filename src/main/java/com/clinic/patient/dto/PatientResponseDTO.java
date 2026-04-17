package com.clinic.patient.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PatientResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
}
