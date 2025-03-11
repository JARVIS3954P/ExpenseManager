package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.ApprovalDTO;

public interface ApprovalService {
    ApprovalDTO approveExpense(Long expenseId, String approverEmail);
    ApprovalDTO rejectExpense(Long expenseId, String approverEmail, String reason);
} 