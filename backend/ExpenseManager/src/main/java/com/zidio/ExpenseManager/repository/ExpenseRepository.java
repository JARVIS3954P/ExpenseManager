package com.zidio.ExpenseManager.repository;

import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByStatus(ExpenseStatus status);
}
