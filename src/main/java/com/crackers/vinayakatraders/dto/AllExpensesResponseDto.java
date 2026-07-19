package com.crackers.vinayakatraders.dto;

import java.util.List;

public record AllExpensesResponseDto(
        List<ExpensesDto> expenses,
        Integer total
) {
}
