package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.ExpenseRequestDto;
import com.crackers.vinayakatraders.entity.Expenses;
import com.crackers.vinayakatraders.repository.ExpensesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;

    public List<Expenses> getAllExpenses() {
        return this.expensesRepository.findAll();
    }

    public Integer sumOfAllExpenses() {
        return this.expensesRepository.sumOfAmount();
    }

    public Expenses addExpense(ExpenseRequestDto request) {
        Expenses expenses = new Expenses();
        expenses.setReasonForExpense(request.reasonForExpense());
        expenses.setAmount(request.amount());

        return this.expensesRepository.save(expenses);
    }

}
