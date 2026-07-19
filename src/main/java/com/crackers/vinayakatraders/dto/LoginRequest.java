package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is a required field") @Email(message = "Field must be an email") String email,
        @NotBlank(message = "Password is a required field") String password
) {
}
