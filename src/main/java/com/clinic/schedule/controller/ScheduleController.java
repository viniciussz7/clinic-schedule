package com.clinic.schedule.controller;

import com.clinic.schedule.dto.CreateScheduleRequestDTO;
import com.clinic.schedule.dto.ScheduleResponseDTO;
import com.clinic.schedule.service.ScheduleService;
import com.clinic.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Doctor Schedules",
        description = "Endpoints for doctors to manage their schedules")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/doctor/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "Create a new schedule for the authenticated doctor.")
    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> create(@Valid @RequestBody CreateScheduleRequestDTO request, Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.create(request, authenticatedUser));
    }

    @Operation(summary = "List all schedules for the authenticated doctor.")
    @GetMapping
    public List<ScheduleResponseDTO> listMySchedules(Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return scheduleService.listMySchedules(authenticatedUser);
    }

    @Operation(summary = "Deactivate a schedule by its ID for the authenticated doctor.")
    @PatchMapping("/{scheduleId}/deactivate")
    public ResponseEntity<ScheduleResponseDTO> deactivate(@PathVariable UUID scheduleId, Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(scheduleService.deactivate(scheduleId, authenticatedUser));
    }

    @Operation(summary = "Activate a schedule by its ID for the authenticated doctor.")
    @PatchMapping("/{scheduleId}/activate")
    public ResponseEntity<ScheduleResponseDTO> activate(@PathVariable UUID scheduleId, Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(scheduleService.activate(scheduleId, authenticatedUser));
    }
}