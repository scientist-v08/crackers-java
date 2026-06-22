package com.crackers.vinayakatraders.dto;

public record JwtResponse(
        String token,
        String type,
        String email,
        String roleName
) {
}
