package com.example.linkshortner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email is required")
        @NotEmpty(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @NotEmpty(message = "Password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long")
        String password
) {
}
