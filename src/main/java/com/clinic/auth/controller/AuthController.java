package com.clinic.auth.controller;

import com.clinic.auth.dto.LoginRequestDTO;
import com.clinic.auth.dto.LoginResponseDTO;
import com.clinic.auth.dto.MeResponseDTO;
import com.clinic.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> me(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(authService.me(userId));
    }
}
