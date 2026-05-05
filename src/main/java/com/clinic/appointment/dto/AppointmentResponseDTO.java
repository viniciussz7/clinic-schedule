package com.clinic.appointment.dto;

import com.clinic.appointment.model.AppointmentStatus;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AppointmentResponseDTO(
        UUID id,

        UUID patientId,
        String patientName,

        UUID doctorId,
        String doctorName,

        LocalDateTime appointmentAt,

        AppointmentStatus status,

        String notes
) {
}
