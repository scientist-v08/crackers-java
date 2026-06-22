package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.NotBlank;

public record BillItemsRequest(
        Integer slNo,
        @NotBlank String item,
        Integer mrpOrNet,
        Integer quantity,
        String discount,
        Integer subTotal
) {
}
