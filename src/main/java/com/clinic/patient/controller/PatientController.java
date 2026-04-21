package com.clinic.patient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
