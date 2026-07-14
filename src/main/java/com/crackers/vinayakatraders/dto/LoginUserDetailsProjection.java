package com.crackers.vinayakatraders.dto;

public record LoginUserDetailsProjection(
        String email,
        String password,
        Long roleId,
        String roleName,
        Long routeId,
        String route,
        String heading
) {}
