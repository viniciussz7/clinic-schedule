package com.clinic.specialty.controller;

import com.clinic.specialty.dto.SpecialtyResponseDTO;
import com.clinic.specialty.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Specialties",
        description = "Endpoints for listing medical specialties")
@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Operation(summary = "List all active medical specialties available in the clinic.")
    @GetMapping
    public List<SpecialtyResponseDTO> list() {
        return specialtyService.listActive();
    }
}
