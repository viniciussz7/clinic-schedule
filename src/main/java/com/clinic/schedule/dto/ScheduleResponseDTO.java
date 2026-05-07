package com.clinic.schedule.dto;

import com.clinic.schedule.model.ScheduleDay;

import java.time.LocalTime;
import java.util.UUID;

public record ScheduleResponseDTO(
        UUID id,
        ScheduleDay dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Boolean active
) {}
