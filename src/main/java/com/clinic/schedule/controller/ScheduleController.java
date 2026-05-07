package com.clinic.schedule.controller;

import com.clinic.schedule.dto.CreateScheduleRequestDTO;
import com.clinic.schedule.dto.ScheduleResponseDTO;
import com.clinic.schedule.service.ScheduleService;
import com.clinic.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> create(@Valid @RequestBody CreateScheduleRequestDTO request, Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.create(request, authenticatedUser));
    }

    @GetMapping
    public List<ScheduleResponseDTO> listMySchedules(Authentication authentication) {

        User authenticatedUser = (User) authentication.getPrincipal();

        return scheduleService.listMySchedules(authenticatedUser);
    }
}