package com.crackers.vinayakatraders.dto;

public record LoginUserDetailsProjection(
        String email,
        String password,
        Short roleId,
        String roleName,
        Short routeId,
        String route,
        String heading
) {}
