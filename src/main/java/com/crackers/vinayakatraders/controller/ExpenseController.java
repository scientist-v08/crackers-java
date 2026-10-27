package com.crackers.vinayakatraders.controller;

import com.crackers.vinayakatraders.dto.AllExpensesResponseDto;
import com.crackers.vinayakatraders.dto.ExpenseAddedResponseDto;
import com.crackers.vinayakatraders.dto.ExpenseRequestDto;
import com.crackers.vinayakatraders.dto.ExpensesDto;
import com.crackers.vinayakatraders.service.ExpensesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

@RestController
@RequestMapping("/admin/expenses")
@AllArgsConstructor
public class ExpenseController {

    private final ExpensesService expensesService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ExpenseAddedResponseDto> addExpense(@Valid @RequestBody ExpenseRequestDto request) {
        this.expensesService.addExpense(request);
        ExpenseAddedResponseDto expenseAddedResponseDto = new ExpenseAddedResponseDto("Expense added");

        return ResponseEntity.ok().body(expenseAddedResponseDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<AllExpensesResponseDto> getAllExpenses() {
        List<ExpensesDto> expenses = this.expensesService.getAllExpenses().stream()
                .map(exp -> new ExpensesDto(exp.getId(), exp.getReasonForExpense(), exp.getAmount()))
                .toList();
        Integer total = this.expensesService.computeSumOfAllExpenses(expenses);
        AllExpensesResponseDto response = new AllExpensesResponseDto(expenses, total);
        return ResponseEntity.ok().body(response);
    }

}
