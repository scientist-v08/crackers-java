package com.crackers.vinayakatraders.dto;

import java.util.List;

public record LoginResponse(
        String access_token,
        List<RouteResponse> routes,
        boolean isAdmin
) {
}
