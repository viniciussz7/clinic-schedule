package com.clinic.schedule.dto;

import com.clinic.schedule.model.ScheduleDay;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateScheduleRequestDTO(
        @NotNull
        ScheduleDay dayOfWeek,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime
) {}
