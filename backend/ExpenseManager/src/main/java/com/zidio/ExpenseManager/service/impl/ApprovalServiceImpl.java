package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.model.Approval;
import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.ApprovalRepository;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.service.interfaces.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public Approval approveExpense(Long expenseId, Long approvedById, String remarks) {
        // Retrieve the expense and user
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        User approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new Approval object and set the approval status
        Approval approval = Approval.builder()
                .approved(true)
                .rejectionReason(null) // Null because the expense is approved
                .approvedAt(LocalDateTime.now())
                .remarks(remarks)
                .approvedBy(approvedBy)
                .expense(expense)
                .build();

        // Save the approval to the database
        return approvalRepository.save(approval);
    }

    @Override
    public Approval rejectExpense(Long expenseId, Long approvedById, String rejectionReason, String remarks) {
        // Retrieve the expense and user
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        User approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new Approval object and set the rejection status
        Approval approval = Approval.builder()
                .approved(false)
                .rejectionReason(rejectionReason)
                .approvedAt(LocalDateTime.now())
                .remarks(remarks)
                .approvedBy(approvedBy)
                .expense(expense)
                .build();

        // Save the approval to the database
        return approvalRepository.save(approval);
    }
}
