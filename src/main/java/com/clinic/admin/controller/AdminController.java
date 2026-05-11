package com.clinic.admin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Admin Dashboard",
        description = "Endpoints for admin dashboard and management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard() {
        return "Welcome to the Admin Dashboard!";
    }
}
