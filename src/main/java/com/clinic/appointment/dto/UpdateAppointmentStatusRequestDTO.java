package com.clinic.appointment.dto;

import com.clinic.appointment.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequestDTO(
        @NotNull AppointmentStatus status
) {}
