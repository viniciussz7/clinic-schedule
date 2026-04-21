package com.clinic.auth.service;

import com.clinic.auth.dto.LoginRequestDTO;
import com.clinic.auth.dto.LoginResponseDTO;
import com.clinic.auth.dto.MeResponseDTO;
import com.clinic.config.security.jwt.JwtService;
import com.clinic.user.model.User;
import com.clinic.user.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRespository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais Inválidas."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais Inválidas.");
        }

        String token = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }

    public MeResponseDTO me(String userId) {
        User user = userRespository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return MeResponseDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.getActive())
                .build();
    }
}
