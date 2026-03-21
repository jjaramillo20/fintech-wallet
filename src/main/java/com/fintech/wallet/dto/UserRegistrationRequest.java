package com.fintech.wallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Lastname is required")
        String lastName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Currency code is required (e.g., MXN)")
        @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
        String currency // Nuevo campo
) {}
