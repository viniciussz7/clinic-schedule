package com.clinic.doctor.controller;

import com.clinic.doctor.dto.DoctorResponseDTO;
import com.clinic.doctor.service.DoctorService;
import com.clinic.schedule.dto.AvailableSlotResponseDTO;
import com.clinic.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorPublicController {

    private final DoctorService doctorService;
    private final ScheduleService scheduleService;

    @GetMapping
    public List<DoctorResponseDTO> list(@RequestParam(required = false)UUID specialtyId) {
        return doctorService.list(specialtyId);
    }

    @GetMapping("/{doctorId}/available-slots")
    public List<AvailableSlotResponseDTO> getAvailableSlots(@PathVariable UUID doctorId, @RequestParam LocalDate date) {
        return scheduleService.getAvailableSlots(doctorId, date);
    }
}
