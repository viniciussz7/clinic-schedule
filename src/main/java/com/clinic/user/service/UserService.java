package com.clinic.user.service;

import com.clinic.exception.EmailAlreadyExistsException;
import com.clinic.user.dto.UserRequestDTO;
import com.clinic.user.dto.UserResponseDTO;
import com.clinic.user.model.User;
import com.clinic.user.model.UserRole;
import com.clinic.user.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;

    /*
    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRespository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email já existe.");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRespository.save(user);
        return toResponseDTO(savedUser);
    }
*/
    public User createPatientUser(String name, String email, String rawPassword) {
        validateEmail(email);
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(UserRole.PATIENT)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        return userRespository.save(user);
    }

    private void validateEmail(String email) {
        if (userRespository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email já existe.");
        }
    }

    /*
    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
    */

}
