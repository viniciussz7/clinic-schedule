package com.clinic.doctor.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Doctor Dashboard",
        description = "Endpoints for doctor dashboard and management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('DOCTOR')")
    public String dashboard() {
        return "Welcome to the Doctor dashboard!";
    }


}
