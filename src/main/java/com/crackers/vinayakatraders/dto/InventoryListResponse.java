package com.crackers.vinayakatraders.dto;

import java.util.List;

public record InventoryListResponse(
        List<InventoryDto> inventoryItems,
        long total,
        long totalElements
) {
}
