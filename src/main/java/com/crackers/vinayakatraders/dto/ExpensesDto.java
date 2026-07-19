package com.crackers.vinayakatraders.dto;

public record ExpensesDto(
        Long id,
        String reasonForExpense,
        Integer amount
) {
}
