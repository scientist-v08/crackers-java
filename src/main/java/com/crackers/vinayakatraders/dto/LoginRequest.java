package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email String Email,
        @NotBlank String Password
) {
}
