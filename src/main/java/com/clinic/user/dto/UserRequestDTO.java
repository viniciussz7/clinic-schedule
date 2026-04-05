package com.clinic.user.dto;

import com.clinic.user.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private UserRole role;
}
