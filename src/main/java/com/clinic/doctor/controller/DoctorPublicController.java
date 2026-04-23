package com.clinic.doctor.controller;

import com.clinic.doctor.dto.DoctorResponseDTO;
import com.clinic.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorPublicController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorResponseDTO> list(@RequestParam(required = false)UUID specialtyId) {
        return doctorService.list(specialtyId);
    }


}
