package com.clinic.appointment.controller;

import com.clinic.appointment.dto.*;
import com.clinic.appointment.service.AppointmentService;
import com.clinic.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Doctor Appointments",
        description = "Endpoints for doctors to manage their appointments")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/doctor/appointments")
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "List all appointments for the authenticated doctor.")
    @GetMapping
    public ResponseEntity<List<AppointmentDoctorResponseDTO>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.listDoctorAppointments(user));
    }

    @Operation(summary = "Update the status of an appointment by its ID for the authenticated doctor.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDoctorResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAppointmentStatusRequestDTO request,
            @AuthenticationPrincipal User user
            ) {

        return ResponseEntity.ok(appointmentService.updateStatus(id, request.status(), user));
    }
}
