package com.clinic.config;

import com.clinic.user.model.User;
import com.clinic.user.model.UserRole;
import com.clinic.user.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        boolean existsAdmin = userRespository.existsByEmail("admin@clinic.com");

        if (!existsAdmin) {
            User admin = User.builder()
                    .name("System Admin")
                    .email("admin@clinic.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .active(true)
                    .build();

            userRespository.save(admin);
        }

    }
}
