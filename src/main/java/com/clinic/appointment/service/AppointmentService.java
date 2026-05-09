package com.clinic.appointment.service;

import com.clinic.appointment.dto.AppointmentDoctorResponseDTO;
import com.clinic.appointment.dto.AppointmentPatientResponseDTO;
import com.clinic.appointment.dto.AppointmentResponseDTO;
import com.clinic.appointment.dto.CreateAppointmentRequestDTO;
import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.AppointmentStatus;
import com.clinic.appointment.repository.AppointmentRepository;
import com.clinic.doctor.model.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.exception.AppointmentNotFoundException;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.PacientNotFoundException;
import com.clinic.patient.model.Patient;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.schedule.model.ScheduleDay;
import com.clinic.schedule.repository.ScheduleRepository;
import com.clinic.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public AppointmentResponseDTO create( CreateAppointmentRequestDTO request,
                                          User authenticatedUser) {
        Patient patient = patientRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new PacientNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        if (!doctor.getActive()) {
            throw new DoctorNotFoundException("Doctor not found");
        }

        validateDoctorAvailability(doctor, request.appointmentAt());

        validateAppointmentConflicts(doctor, patient, request.appointmentAt());

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentAt(request.appointmentAt())
                .status(AppointmentStatus.SCHEDULED)
                .notes(request.notes())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return toResponse(savedAppointment);
    }

    public List<AppointmentPatientResponseDTO> listMy(User authenticatedUser) {

        Patient patient = patientRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new PacientNotFoundException("Patient not found!"));

        LocalDateTime now = LocalDateTime.now();

        return appointmentRepository.findByPatientIdOrderByAppointmentAtDesc(patient.getId())
                .stream()
                .map(appointment -> toPatientResponse(appointment, now))
                .toList();
    }

    public List<AppointmentDoctorResponseDTO> listDoctorAppointments(User authenticatedUser) {

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        LocalDateTime now = LocalDateTime.now();

        return appointmentRepository.findByDoctorIdOrderByAppointmentAtAsc(doctor.getId())
                .stream()
                .map(appointment -> toDoctorResponse(appointment, now))
                .toList();
    }

    @Transactional
    public AppointmentDoctorResponseDTO updateStatus(
            UUID appointmentId,
            AppointmentStatus newStatus,
            User authenticatedUser
    ){

        Doctor doctor = doctorRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found!"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found!"));

        //medico dono da consulta?
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("You cannot update this appointment.");
        }

        validateStatusTransition(appointment.getStatus(), newStatus);

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        LocalDateTime now = LocalDateTime.now();

        return toDoctorResponse(appointment, now);
    }

    @Transactional
    public AppointmentPatientResponseDTO cancelAppointment(UUID appointmentId, User authenticatedUser) {

        Patient patient = patientRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new PacientNotFoundException("Patient not found!"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found!"));

        //validar paciente
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot cancel this appointment");
        }

        //validar consulta futura
        if (appointment.getAppointmentAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Past appointments cannot be cancelled");
        }

        //validar estado
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new RuntimeException("This appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);

        return toPatientResponse(appointment, LocalDateTime.now());
    }

    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {

        //estados finais
        if (current == AppointmentStatus.COMPLETED ||
            current == AppointmentStatus.CANCELLED ||
            current == AppointmentStatus.NO_SHOW) {

            throw new RuntimeException("Cannot change a finalized appointment.");
        }

        //regras
        switch (current) {
            case SCHEDULED -> {
                if (next != AppointmentStatus.CONFIRMED &&
                        next != AppointmentStatus.CANCELLED &&
                        next != AppointmentStatus.NO_SHOW) {

                    throw new RuntimeException("Invalid transition from SCHEDULED");
                }
            }

            case CONFIRMED -> {
                if (next != AppointmentStatus.COMPLETED &&
                        next != AppointmentStatus.CANCELLED &&
                        next != AppointmentStatus.NO_SHOW) {

                    throw new RuntimeException("Invalid transition from CONFIRMED");
                }
            }

            default -> throw new RuntimeException("Invalid status transition");
        }
    }

    private void validateDoctorAvailability(Doctor doctor, LocalDateTime appointmentAt) {

        ScheduleDay dayOfWeek = ScheduleDay.valueOf(appointmentAt.getDayOfWeek().name());

        LocalTime appointmentTime = appointmentAt.toLocalTime();

        boolean available = scheduleRepository.existsByDoctorIdAndDayOfWeekAndActiveTrueAndStartTimeLessThanEqualAndEndTimeGreaterThan(
                doctor.getId(),
                dayOfWeek,
                appointmentTime,
                appointmentTime
        );

        if (!available) {
            throw new IllegalArgumentException("Doctor is not available at the requested time");
        }
    }

    private void validateAppointmentConflicts(Doctor doctor, Patient patient, LocalDateTime appointmentAt) {

        if (appointmentRepository.existsByDoctorAndAppointmentAt(
                doctor,
                appointmentAt
        )) {
            throw new RuntimeException("Doctor is not available at the requested time");
        }

        if (appointmentRepository.existsByPatientAndAppointmentAt(
                patient,
                appointmentAt
        )) {
            throw new RuntimeException("Patient already has appointment at this time");
        }

    }

    private AppointmentResponseDTO toResponse(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getUser().getName(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getUser().getName(),
                appointment.getAppointmentAt(),
                appointment.getStatus(),
                appointment.getNotes() != null ? appointment.getNotes() : ""
        );
    }

    private AppointmentPatientResponseDTO toPatientResponse(Appointment appointment, LocalDateTime now) {

        boolean isPast = appointment.getAppointmentAt().isBefore(now);

        return new AppointmentPatientResponseDTO(
                appointment.getId(),
                appointment.getDoctor().getUser().getName(),
                appointment.getAppointmentAt(),
                appointment.getStatus(),
                isPast,
                appointment.getNotes() != null ? appointment.getNotes() : ""
        );
    }

    private AppointmentDoctorResponseDTO toDoctorResponse(Appointment appointment, LocalDateTime now) {

        boolean isPast = appointment.getAppointmentAt().isBefore(now);

        return new AppointmentDoctorResponseDTO(
                appointment.getId(),
                appointment.getPatient().getUser().getName(),
                appointment.getAppointmentAt(),
                appointment.getStatus(),
                isPast,
                appointment.getNotes()
        );
    }


}
