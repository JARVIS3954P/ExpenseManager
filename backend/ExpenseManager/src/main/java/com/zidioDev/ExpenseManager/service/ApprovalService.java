package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.ApprovalDTO;

public interface ApprovalService {
    ApprovalDTO approveExpense(Long expenseId, String approverRole);
    ApprovalDTO rejectExpense(Long expenseId, String approverRole, String rejectionReason);
}



