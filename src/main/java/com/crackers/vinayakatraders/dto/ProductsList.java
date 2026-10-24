package com.crackers.vinayakatraders.dto;

import java.util.List;

public record ProductsList(
        String brand,
        List<PriceListResponse> list
) {
}
