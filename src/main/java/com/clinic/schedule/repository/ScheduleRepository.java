package com.clinic.schedule.repository;

import com.clinic.schedule.model.Schedule;
import com.clinic.schedule.model.ScheduleDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    List<Schedule> findByDoctorIdAndActiveTrue(UUID doctorId);

    List<Schedule> findByDoctorIdAndDayOfWeekAndActiveTrue(UUID doctorId, ScheduleDay dayOfWeek);

    boolean existsByDoctorIdAndDayOfWeekAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID doctorId, ScheduleDay dayOfWeek, LocalTime start, LocalTime endTime);
}
