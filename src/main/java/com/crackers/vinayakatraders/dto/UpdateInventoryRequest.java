package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateInventoryRequest(
        @NotNull @Positive Long id,
        String brandOrCompany,
        String item,
        @NotNull @Positive Integer numOfBoxes,
        @NotNull @Positive Integer numOfCartons,
        @NotNull @Positive Integer pricePerCarton,
        String state
) {
}
