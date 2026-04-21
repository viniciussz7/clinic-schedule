package com.clinic.specialty.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpecialtyResponseDTO {

    private String id;
    private String name;
    private Boolean active;

}
