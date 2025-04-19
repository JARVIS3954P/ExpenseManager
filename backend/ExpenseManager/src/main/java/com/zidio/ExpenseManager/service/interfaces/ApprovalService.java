package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.model.Approval;

public interface ApprovalService {
    Approval approveExpense(Long expenseId, Long approvedById, String remarks);
    Approval rejectExpense(Long expenseId, Long approvedById, String rejectionReason, String remarks);
}
