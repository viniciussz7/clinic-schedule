package com.clinic.appointment.controller;

import com.clinic.appointment.dto.AppointmentPatientResponseDTO;
import com.clinic.appointment.dto.AppointmentResponseDTO;
import com.clinic.appointment.dto.CreateAppointmentRequestDTO;
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
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(
            @RequestBody @Valid CreateAppointmentRequestDTO requestDTO,
            @AuthenticationPrincipal User user
    ) {

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(appointmentService.create(requestDTO, user));
    }

    @GetMapping("/me")
    public ResponseEntity<List<AppointmentPatientResponseDTO>> listMy(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.listMy(user));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentPatientResponseDTO> cancel(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, user));
    }
}
