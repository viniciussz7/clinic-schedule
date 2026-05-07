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

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time!");
        }
    }

}
