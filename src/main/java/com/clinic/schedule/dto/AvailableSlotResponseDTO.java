package com.clinic.schedule.dto;

import java.time.LocalTime;

public record AvailableSlotResponseDTO (
        LocalTime time
) {}