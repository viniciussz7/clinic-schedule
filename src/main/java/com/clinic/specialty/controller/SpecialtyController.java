package com.clinic.specialty.controller;

import com.clinic.specialty.dto.SpecialtyResponseDTO;
import com.clinic.specialty.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public List<SpecialtyResponseDTO> list() {
        return specialtyService.listActive();
    }
}
