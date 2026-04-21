package com.clinic.doctor.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('DOCTOR')")
    public String dashboard() {
        return "Welcome to the Doctor dashboard!";
    }
}
