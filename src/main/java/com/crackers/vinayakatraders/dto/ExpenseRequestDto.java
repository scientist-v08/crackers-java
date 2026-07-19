package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ExpenseRequestDto(
        @NotBlank(message = "reasonForExpense is a required field") String reasonForExpense,
        @Min(value = 10, message = "Amount should be at least Rs.10") Integer amount
) {
}
