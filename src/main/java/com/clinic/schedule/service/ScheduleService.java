package com.clinic.schedule.service;

import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.AppointmentStatus;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.doctor.model.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.schedule.dto.AvailableSlotResponseDTO;
import com.clinic.schedule.dto.CreateScheduleRequestDTO;
import com.clinic.schedule.dto.ScheduleResponseDTO;
import com.clinic.schedule.model.Schedule;
import com.clinic.schedule.model.ScheduleDay;
import com.clinic.schedule.repository.ScheduleRepository;
import com.clinic.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private static final int SLOT_DURATION_MINUTES = 30;
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

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

    @Transactional(readOnly = true)
    public List<AvailableSlotResponseDTO> getAvailableSlots(UUID doctorId, LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        ScheduleDay day = ScheduleDay.valueOf(date.getDayOfWeek().name());

        List<Schedule> schedulesActives = scheduleRepository.findByDoctorIdAndDayOfWeekAndActiveTrue(
                doctor.getId(),
                day
        );

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<AppointmentStatus> occupiedStatuses = List.of(
                AppointmentStatus.SCHEDULED,
                AppointmentStatus.CONFIRMED
        );

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentAtBetweenAndStatusIn(
                doctor.getId(),
                startOfDay,
                endOfDay,
                occupiedStatuses
        );

        return generateAvailableSlots(schedulesActives, getOccupiedSlots(appointments));
    }

    private Set<LocalTime> getOccupiedSlots(List<Appointment> appointments) {

        Set<LocalTime> occupiedSlots = appointments.stream()
                .map(appointment -> appointment.getAppointmentAt().toLocalTime())
                .collect(Collectors.toSet());

        return occupiedSlots;
    }

    private List<AvailableSlotResponseDTO> generateAvailableSlots(List<Schedule> schedulesActives, Set<LocalTime> occupiedSlots) {
        List<AvailableSlotResponseDTO> availableSlots = new ArrayList<>();

        for (Schedule schedule : schedulesActives) {
            LocalTime current = schedule.getStartTime();
            while (current.plusMinutes(SLOT_DURATION_MINUTES).isBefore(schedule.getEndTime())
                    || current.plusMinutes(SLOT_DURATION_MINUTES).equals(schedule.getEndTime())) {
                if (!occupiedSlots.contains(current)) {
                    availableSlots.add(new AvailableSlotResponseDTO(current));
                }
                current = current.plusMinutes(SLOT_DURATION_MINUTES);
            }
        }

        return availableSlots;
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
