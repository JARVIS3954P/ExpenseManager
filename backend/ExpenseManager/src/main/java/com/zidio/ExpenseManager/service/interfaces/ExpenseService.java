package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.dto.ExpenseRequestDTO;
import com.zidio.ExpenseManager.dto.ExpenseResponseDTO;
import com.zidio.ExpenseManager.dto.ExpenseUpdateStatusDTO;

import java.util.List;

public interface ExpenseService {
    ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO);
    ExpenseResponseDTO getExpenseById(Long id);
    List<ExpenseResponseDTO> getAllExpenses();
    ExpenseResponseDTO updateExpenseStatus(Long expenseId, ExpenseUpdateStatusDTO statusDTO);
    void deleteExpense(Long id);

    List<ExpenseResponseDTO> getExpensesForApproval();
}
