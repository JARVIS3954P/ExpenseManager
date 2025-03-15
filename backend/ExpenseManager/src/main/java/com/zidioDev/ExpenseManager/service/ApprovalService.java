package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;

import java.util.List;

public interface ApprovalService {
    ApprovalDTO approveExpense(ApprovalDTO approvalDTO);
    ApprovalDTO rejectExpense(ApprovalDTO approvalDTO);
    List<ApprovalDTO> getPendingApprovals(String role);
}



