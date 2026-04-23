package com.clinic.doctor.service;

import com.clinic.doctor.dto.CreateDoctorRequestDTO;
import com.clinic.doctor.dto.DoctorResponseDTO;
import com.clinic.doctor.model.Doctor;
import com.clinic.doctor.repository.DoctorRepository;
import com.clinic.exception.CrmAlreadyExistsException;
import com.clinic.exception.EmailAlreadyExistsException;
import com.clinic.exception.SpecialtyNotFoundException;
import com.clinic.specialty.dto.SpecialtyResponseDTO;
import com.clinic.specialty.model.Specialty;
import com.clinic.specialty.repository.SpecialtyRepository;
import com.clinic.user.model.User;
import com.clinic.user.model.UserRole;
import com.clinic.user.repository.UserRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRespository userRespository;
    private final SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DoctorResponseDTO create(CreateDoctorRequestDTO doctorRequestDTO) {

        if (userRespository.existsByEmail(doctorRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email já existe.");
        }

        if (doctorRepository.existsByCrmIgnoreCase(doctorRequestDTO.getCrm())) {
            throw new CrmAlreadyExistsException("CRM já existe.");
        }

        Specialty specialty = specialtyRepository.findById(doctorRequestDTO.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("Especialidade não encontrada."));

        User user = User.builder()
                .name(doctorRequestDTO.getName().trim())
                .email(doctorRequestDTO.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(doctorRequestDTO.getPassword()))
                .role(UserRole.DOCTOR)
                .active(true)
                .build();

        User savedUser = userRespository.save(user);

        Doctor doctor = Doctor.builder()
                .user(savedUser)
                .crm(doctorRequestDTO.getCrm().trim())
                .specialty(specialty)
                .bio(doctorRequestDTO.getBio())
                .active(true)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return toResponse(savedDoctor);
    }

    private DoctorResponseDTO toResponse(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId().toString())
                .name(doctor.getUser().getName())
                .email(doctor.getUser().getEmail())
                .crm(doctor.getCrm())
                .bio(doctor.getBio())
                .active(doctor.getActive())
                .specialty(
                        SpecialtyResponseDTO.builder()
                                .id(doctor.getSpecialty().getId().toString())
                                .name(doctor.getSpecialty().getName())
                                .active(doctor.getSpecialty().getActive())
                                .build()
                )
                .build();
    }

    public List<DoctorResponseDTO> list(UUID specialtyId) {

        List<Doctor> doctors;

        if (specialtyId == null) {
            doctors = doctorRepository.findByActiveTrueOrderByUserNameAsc();
        } else {
            doctors = doctorRepository.findBySpecialtyIdAndActiveTrueOrderByUserNameAsc(specialtyId);
        }
        return doctors
                .stream()
                .map(this::toResponse)
                .toList();
    }

}
