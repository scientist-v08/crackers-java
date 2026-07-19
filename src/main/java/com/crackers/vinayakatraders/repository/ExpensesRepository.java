package com.crackers.vinayakatraders.repository;

import com.crackers.vinayakatraders.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expenses e")
    Integer sumOfAmount();
}
