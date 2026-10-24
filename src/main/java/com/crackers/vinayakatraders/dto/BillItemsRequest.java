package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BillItemsRequest(
        @NotNull(message = "Serial number is required") @Positive(message = "Serial number must be positive") Integer slNo,
        @NotBlank(message = "Item is a required field") String item,
        @NotNull(message = "MRP/Net is required") @PositiveOrZero(message = "MRP/Net cannot be negative") Integer mrpOrNet,
        @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be greater than zero") Integer quantity,
        String discount,
        Integer subTotal
) {
}
