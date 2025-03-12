package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;

public interface ApprovalService {
    ApprovalDTO approveExpense(ApprovalDTO approvalDTO);
    ApprovalDTO rejectExpense(ApprovalDTO approvalDTO);
}



