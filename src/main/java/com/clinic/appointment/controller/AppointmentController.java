package com.clinic.appointment.controller;

import com.clinic.appointment.dto.AppointmentPatientResponseDTO;
import com.clinic.appointment.dto.AppointmentResponseDTO;
import com.clinic.appointment.dto.CreateAppointmentRequestDTO;
import com.clinic.appointment.service.AppointmentService;
import com.clinic.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        name = "Patient Appointments",
        description = "Endpoints for patients to manage their appointments")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Create a new appointment for the authenticated patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment scheduled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid appointment request"),
            @ApiResponse(responseCode = "404", description = "Doctor or patient or schedule not found"),
            @ApiResponse(responseCode = "409", description = "Appointment time conflict")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(
            @RequestBody @Valid CreateAppointmentRequestDTO requestDTO,
            @AuthenticationPrincipal User user
    ) {

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(appointmentService.create(requestDTO, user));
    }

    @Operation(summary = "List all appointments for the authenticated patient.")
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentPatientResponseDTO>> listMy(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.listMy(user));
    }

    @Operation(summary = "Cancel an appointment by its ID for the authenticated patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Appointment cannot be cancelled"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentPatientResponseDTO> cancel(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, user));
    }
}
