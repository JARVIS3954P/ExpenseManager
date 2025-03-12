package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    List<ExpenseDTO> getExpensesByUser(Long userId);
    void deleteExpense(Long expenseId);
    ExpenseDTO getExpenseById(Long id);
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
}



