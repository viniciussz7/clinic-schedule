package com.clinic.patient.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Patient Dashboard",
        description = "Endpoints for patient dashboard and management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PATIENT')")
    public String dashboard() {
        return "Welcome to the Patient Dashboard!";
    }

}
