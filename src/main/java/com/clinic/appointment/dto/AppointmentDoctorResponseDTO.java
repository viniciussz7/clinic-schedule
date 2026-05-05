package com.clinic.appointment.dto;

import com.clinic.appointment.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AppointmentDoctorResponseDTO(
        UUID id,

        String patientName,

        LocalDateTime appointmentAt,

        AppointmentStatus status,

        boolean isPast,

        String notes
) {
}
