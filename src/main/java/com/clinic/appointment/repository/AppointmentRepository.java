package com.clinic.appointment.repository;

import com.clinic.appointment.model.Appointment;
import com.clinic.appointment.model.AppointmentStatus;
import com.clinic.doctor.model.Doctor;
import com.clinic.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    //medico ja tem consulta nesse horario?
    boolean existsByDoctorAndAppointmentAt(Doctor doctor, LocalDateTime appointmentAt);

    //impede paciente de marcar duas consultas no mesmo horario
    boolean existsByPatientAndAppointmentAt(Patient patient, LocalDateTime appointmentAt);

    //historico do paciente
    List<Appointment> findByPatientIdOrderByAppointmentAtDesc(UUID patientId);

    //agenda do medico
    List<Appointment> findByDoctorIdOrderByAppointmentAtAsc(UUID doctorId);

    //agenda filtrada por status
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentAtAsc(UUID doctorId, AppointmentStatus status);

    List<Appointment> findByPatientIdAndStatusOrderByAppointmentAtDesc(UUID patientId, AppointmentStatus status);

}
