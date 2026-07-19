package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.NotBlank;

public record BillItemsRequest(
        Integer slNo,
        @NotBlank(message = "Item is a required field") String item,
        Integer mrpOrNet,
        Integer quantity,
        String discount,
        Integer subTotal
) {
}
