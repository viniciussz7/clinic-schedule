package com.clinic.doctor.dto;

import com.clinic.specialty.dto.SpecialtyResponseDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorResponseDTO {
    private String id;
    private String name;
    private String email;
    private String crm;
    private SpecialtyResponseDTO specialty;
    private String bio;
    private Boolean active;
}
