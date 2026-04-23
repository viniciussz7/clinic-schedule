package com.clinic.admin.controller;

import com.clinic.doctor.dto.CreateDoctorRequestDTO;
import com.clinic.doctor.dto.DoctorResponseDTO;
import com.clinic.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> create(@RequestBody @Valid CreateDoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO response = doctorService.create(doctorRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
