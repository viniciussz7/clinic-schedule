package com.clinic.appointment.controller;

import com.clinic.appointment.dto.AppointmentDoctorResponseDTO;
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

@RestController
@RequestMapping("/doctor/appointments")
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDoctorResponseDTO>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.listDoctorAppointments(user));
    }
}
