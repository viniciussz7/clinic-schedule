package com.clinic.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeResponseDTO {

    private String id;
    private String name;
    private String email;
    private String role;
    private Boolean active;

}
