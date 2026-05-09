package com.clinic.schedule.service;

import com.clinic.doctor.model.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.schedule.dto.CreateScheduleRequestDTO;
import com.clinic.schedule.dto.ScheduleResponseDTO;
import com.clinic.schedule.model.Schedule;
import com.clinic.schedule.repository.ScheduleRepository;
import com.clinic.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public ScheduleResponseDTO create(CreateScheduleRequestDTO request, User authenticatedUser) {

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        validateTimeRange(request.startTime(), request.endTime());

        validateOverlap(doctor, request);

        Schedule schedule = Schedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .active(true)
                .build();

        scheduleRepository.save(schedule);

        return toResponse(schedule);
    }

    public ScheduleResponseDTO deactivate(UUID scheduleId, User authenticatedUser) {

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId()).
                orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found!"));

        validateScheduleOwnership(schedule, doctor);

        if (!schedule.getActive()) {
            throw new IllegalArgumentException("Schedule is already inactive!");
        }

        schedule.setActive(false);
        scheduleRepository.save(schedule);
        return toResponse(schedule);
    }

    public ScheduleResponseDTO activate(UUID scheduleId, User authenticatedUser) {

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId()).
                orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found!"));

        validateScheduleOwnership(schedule, doctor);

        if (schedule.getActive()) {
            throw new IllegalArgumentException("Schedule is already active!");
        }

        schedule.setActive(true);
        scheduleRepository.save(schedule);
        return toResponse(schedule);
    }


    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> listMySchedules(User authenticatedUser) {

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        return scheduleRepository
                .findByDoctorIdAndActiveTrue(doctor.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ScheduleResponseDTO toResponse(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getActive()
        );
    }

    private void validateScheduleOwnership(Schedule schedule, Doctor doctor) {

        if (!schedule.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("You can only manage your own schedules!");
        }
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time!");
        }
    }

    private void validateOverlap(Doctor doctor, CreateScheduleRequestDTO request) {

        boolean hasConflict = scheduleRepository.existsByDoctorIdAndDayOfWeekAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThan(
                doctor.getId(),
                request.dayOfWeek(),
                request.endTime(),
                request.startTime()
        );

        if (hasConflict) {
            throw new IllegalArgumentException("Schedule overlaps with existing availability!");
        }
    }

}
