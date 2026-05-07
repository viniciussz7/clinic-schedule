package com.clinic.appointment.controller;

import com.clinic.appointment.dto.*;
import com.clinic.appointment.service.AppointmentService;
import com.clinic.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor/appointments")
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDoctorResponseDTO>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.listDoctorAppointments(user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDoctorResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAppointmentStatusRequestDTO request,
            @AuthenticationPrincipal User user
            ) {

        return ResponseEntity.ok(appointmentService.updateStatus(id, request.status(), user));
    }
}
