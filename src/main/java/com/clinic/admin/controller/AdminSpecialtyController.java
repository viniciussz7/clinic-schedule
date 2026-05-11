package com.clinic.admin.controller;

import com.clinic.specialty.dto.CreateSpecialtyRequestDTO;
import com.clinic.specialty.dto.SpecialtyResponseDTO;
import com.clinic.specialty.service.SpecialtyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        name = "Admin Specialties",
        description = "Endpoints for administrators to manage medical specialties")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin/specialties")
@RequiredArgsConstructor
public class AdminSpecialtyController {

    private final SpecialtyService  specialtyService;

    @PostMapping
    public ResponseEntity<SpecialtyResponseDTO> create(@RequestBody @Valid CreateSpecialtyRequestDTO requestDTO) {
        SpecialtyResponseDTO response = specialtyService.create(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
