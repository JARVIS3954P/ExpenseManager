package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.config.ApprovalConfig;
import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.dto.NotificationDTO;
import com.zidioDev.ExpenseManager.model.Expense;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.repository.ExpenseRepository;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.ExpenseService;
import com.zidioDev.ExpenseManager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ApprovalConfig approvalConfig;

    @Override
    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(BigDecimal.valueOf(expenseDTO.getAmount()));
        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(LocalDateTime.now());
        expense.setCreatedAt(LocalDateTime.now());

        // Set initial status based on amount
        if (expense.getAmount().compareTo(BigDecimal.valueOf(approvalConfig.getAutoApprovalThreshold())) <= 0) {
            expense.setStatus(ApprovalStatus.APPROVED);
            // Send auto-approval notification
            notificationService.sendNotification(NotificationDTO.builder()
                    .recipientId(user.getId().toString())
                    .message("Your expense " + expense.getTitle() + " has been automatically approved")
                    .read(false)
                    .build());
        } else if (expense.getAmount().compareTo(BigDecimal.valueOf(approvalConfig.getManagerApprovalThreshold())) <= 0) {
            expense.setStatus(ApprovalStatus.PENDING);
            // Notify managers
            notifyManagers(expense, "New expense requires manager approval");
        } else {
            expense.setStatus(ApprovalStatus.PENDING);
            // Notify admins
            notifyAdmins(expense, "New expense requires admin approval");
        }

        expense = expenseRepository.save(expense);
        return mapToDTO(expense);
    }

    private void notifyManagers(Expense expense, String message) {
        List<User> managers = userRepository.findByRole(Role.MANAGER);
        managers.forEach(manager -> notificationService.sendNotification(NotificationDTO.builder()
                .recipientId(manager.getId().toString())
                .message(message + ": " + expense.getTitle() + " ($" + expense.getAmount() + ")")
                .read(false)
                .build()));
    }

    private void notifyAdmins(Expense expense, String message) {
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        admins.forEach(admin -> notificationService.sendNotification(NotificationDTO.builder()
                .recipientId(admin.getId().toString())
                .message(message + ": " + expense.getTitle() + " ($" + expense.getAmount() + ")")
                .read(false)
                .build()));
    }

    @Override
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }

    @Override
    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapToDTO(expense);
    }

    @Override
    @Transactional
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(BigDecimal.valueOf(expenseDTO.getAmount()));
        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(expenseDTO.getDate());

        expense = expenseRepository.save(expense);
        return mapToDTO(expense);
    }

    private ExpenseDTO mapToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount().doubleValue())
                .date(expense.getDate())
                .category(expense.getCategory())
                .userId(expense.getUser().getId())
                .description(expense.getDescription())
                .status(expense.getStatus())
                .reviewerId(expense.getReviewer() != null ? expense.getReviewer().getId() : null)
                .rejectionReason(expense.getRejectionReason())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}



