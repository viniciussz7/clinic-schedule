package com.clinic.doctor.controller;

import com.clinic.doctor.dto.DoctorResponseDTO;
import com.clinic.doctor.service.DoctorService;
import com.clinic.schedule.dto.AvailableSlotResponseDTO;
import com.clinic.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Public Doctors ",
        description = "Public endpoints for listing doctors and their available slots")
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorPublicController {

    private final DoctorService doctorService;
    private final ScheduleService scheduleService;

    @Operation(summary = "List all doctors, optionally filtered by specialty ID.")
    @GetMapping
    public List<DoctorResponseDTO> list(@RequestParam(required = false)UUID specialtyId) {
        return doctorService.list(specialtyId);
    }

    @Operation(summary = "Get available appointment slots for a specific doctor on a given date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available slots"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/{doctorId}/available-slots")
    public List<AvailableSlotResponseDTO> getAvailableSlots(@PathVariable UUID doctorId, @RequestParam LocalDate date) {
        return scheduleService.getAvailableSlots(doctorId, date);
    }
}
