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
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.PacientNotFoundException;
import com.clinic.patient.model.Patient;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

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

        if (appointmentRepository.existsByDoctorAndAppointmentAt(
                doctor,
                request.appointmentAt()
        )) {
            throw new RuntimeException("Doctor is not available at the requested time");
        }

        if (appointmentRepository.existsByPatientAndAppointmentAt(
                patient,
                request.appointmentAt()
        )) {
            throw new RuntimeException("Patient already has appointment at this time");
        }

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
