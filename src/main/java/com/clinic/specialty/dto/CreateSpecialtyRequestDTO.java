package com.clinic.specialty.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSpecialtyRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

}
