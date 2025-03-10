package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.ApprovalDTO;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ApprovalDTO approveExpense(Long expenseId, String approverRole) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (expense.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Expense is not in PENDING state");
        }

        expense.setStatus(ApprovalStatus.APPROVED);
        expense.setUpdatedAt(LocalDateTime.now());
        expenseRepository.save(expense);

        // Send notification to expense creator
        NotificationDTO notification = NotificationDTO.builder()
                .recipientId(expense.getUser().getId().toString())
                .message("Your expense " + expense.getTitle() + " has been approved")
                .read(false)
                .build();
        notificationService.sendNotification(notification);

        return new ApprovalDTO(expense.getId(), approverRole, true, null, LocalDateTime.now());
    }

    @Override
    @Transactional
    public ApprovalDTO rejectExpense(Long expenseId, String approverRole, String rejectionReason) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (expense.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException("Expense is not in PENDING state");
        }

        expense.setStatus(ApprovalStatus.REJECTED);
        expense.setRejectionReason(rejectionReason);
        expense.setUpdatedAt(LocalDateTime.now());
        expenseRepository.save(expense);

        // Send notification to expense creator
        NotificationDTO notification = NotificationDTO.builder()
                .recipientId(expense.getUser().getId().toString())
                .message("Your expense " + expense.getTitle() + " has been rejected. Reason: " + rejectionReason)
                .read(false)
                .build();
        notificationService.sendNotification(notification);

        return new ApprovalDTO(expense.getId(), approverRole, false, rejectionReason, LocalDateTime.now());
    }
}

