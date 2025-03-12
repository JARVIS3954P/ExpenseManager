package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;
import com.zidioDev.ExpenseManager.dto.NotificationDTO;
import com.zidioDev.ExpenseManager.model.Expense;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.repository.ExpenseRepository;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import com.zidioDev.ExpenseManager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ApprovalDTO approveExpense(ApprovalDTO approvalDTO) {
        Expense expense = expenseRepository.findById(approvalDTO.getExpenseId())
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        // TODO: Get reviewer from security context
        User reviewer = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        expense.setStatus(ApprovalStatus.APPROVED);
        expense.setReviewer(reviewer);
        expenseRepository.save(expense);

        // Send notification to the expense owner
        notificationService.sendNotification(NotificationDTO.builder()
                .recipientId(expense.getUser().getId().toString())
                .message("Your expense has been approved")
                .build());

        return ApprovalDTO.builder()
                .expenseId(expense.getId())
                .approverRole(approvalDTO.getApproverRole())
                .approved(true)
                .build();
    }

    @Override
    @Transactional
    public ApprovalDTO rejectExpense(ApprovalDTO approvalDTO) {
        Expense expense = expenseRepository.findById(approvalDTO.getExpenseId())
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        // TODO: Get reviewer from security context
        User reviewer = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        expense.setStatus(ApprovalStatus.REJECTED);
        expense.setReviewer(reviewer);
        expense.setRejectionReason(approvalDTO.getRejectionReason());
        expenseRepository.save(expense);

        // Send notification to the expense owner
        notificationService.sendNotification(NotificationDTO.builder()
                .recipientId(expense.getUser().getId().toString())
                .message("Your expense has been rejected: " + approvalDTO.getRejectionReason())
                .build());

        return ApprovalDTO.builder()
                .expenseId(expense.getId())
                .approverRole(approvalDTO.getApproverRole())
                .approved(false)
                .rejectionReason(approvalDTO.getRejectionReason())
                .build();
    }
}



