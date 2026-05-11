package com.clinic.patient.controller;

import com.clinic.patient.dto.PatientRegisterRequestDTO;
import com.clinic.patient.dto.PatientResponseDTO;
import com.clinic.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Patient Registration",
        description = "Endpoint for patient registration and public access")
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientPublicController {

    private final PatientService patientService;

    @Operation(summary = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient registered successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid registration data.")
    })
    @PostMapping("/register")
    public ResponseEntity<PatientResponseDTO> register(@RequestBody @Valid PatientRegisterRequestDTO dto) {
        PatientResponseDTO response = patientService.register(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
