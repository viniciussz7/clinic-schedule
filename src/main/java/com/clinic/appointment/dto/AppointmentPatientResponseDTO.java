package com.clinic.appointment.dto;

import com.clinic.appointment.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AppointmentPatientResponseDTO(
        UUID id,
        String doctorName,
        LocalDateTime appointmentAt,
        AppointmentStatus status,
        boolean isPast,
        String notes
) {
}
