package com.clinic.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentRequestDTO(

        @NotNull(message = "Doctor id is required")
        UUID doctorId,

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment must be in the future")
        LocalDateTime appointmentAt,

        @Size(max = 500, message = "Notes must have at most 500 characters")
        String notes

) {
}

