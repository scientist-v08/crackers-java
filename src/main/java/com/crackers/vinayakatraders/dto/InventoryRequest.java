package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.*;

public record InventoryRequest(
        String brandOrCompany,
        @NotBlank
        @Pattern(regexp = "(?i)ORDERED|RECEIVED|UNPACKED",
                message = "state must be Ordered, Received, or Unpacked")
        String state,
        String item,
        @NotNull @Positive Integer numOfBoxes,
        @NotNull @Positive Integer numOfCartons,
        @NotNull @Positive Integer pricePerCarton,
        // Kept for API compatibility; the backend recalculates subTotal.
        @NotNull @Positive Integer subtotal
) {
}
