package com.clinic.patient.controller;

import com.clinic.patient.dto.PatientRegisterRequestDTO;
import com.clinic.patient.dto.PatientResponseDTO;
import com.clinic.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientPublicController {

    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponseDTO> register(@RequestBody @Valid PatientRegisterRequestDTO dto) {
        PatientResponseDTO response = patientService.register(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
