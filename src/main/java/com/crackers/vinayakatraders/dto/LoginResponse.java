package com.crackers.vinayakatraders.dto;

import com.crackers.vinayakatraders.entity.Routes;

import java.util.List;

public record LoginResponse(
        String access_token,
        List<Routes> routes
) {
}
