package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.ApprovalDTO;
import com.zidioDev.ExpenseManager.model.Expense;
import com.zidioDev.ExpenseManager.repository.ExpenseRepository;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ExpenseRepository expenseRepository;

    @Override
    public ApprovalDTO approveExpense(Long expenseId, String approverRole) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setStatus("APPROVED");
        expenseRepository.save(expense);

        return new ApprovalDTO(expense.getId(), approverRole, true, null, LocalDateTime.now());
    }
}

