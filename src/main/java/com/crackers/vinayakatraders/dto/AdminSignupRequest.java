package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminSignupRequest(
        @NotBlank(message = "Email is a required field") @Email(message = "Field must be an email") String email,
        @NotBlank(message = "Password is a required field") @Size(min = 8, max = 15, message = "Password should be at least 8 characters long and utmost 15 characters long") String password,
        String fullName
) {
}
