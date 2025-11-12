package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.ExpenseRequestDTO;
import com.zidio.ExpenseManager.dto.ExpenseResponseDTO;
import com.zidio.ExpenseManager.dto.ExpenseUpdateStatusDTO;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import com.zidio.ExpenseManager.enums.UserRole;
import com.zidio.ExpenseManager.model.Approval;
import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.service.interfaces.ApprovalService;
import com.zidio.ExpenseManager.service.interfaces.ExpenseService;
import com.zidio.ExpenseManager.service.interfaces.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ApprovalService approvalService;
    private final FileStorageService fileStorageService;

    private static final BigDecimal AUTO_APPROVAL_THRESHOLD = new BigDecimal("5000.00"); // ₹5000

    private ExpenseResponseDTO mapToDTO(Expense expense) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .status(expense.getStatus())
                .category(expense.getCategory())
                .userId(expense.getUser().getId())
                .attachmentUrl(expense.getAttachmentUrl())
                .expenseDate(expense.getExpenseDate())
                .build();
    }

    private Expense mapToEntity(ExpenseRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Expense.builder()
                .title(dto.getTitle())
                .amount(BigDecimal.valueOf(dto.getAmount()))
                .category(dto.getCategory())
                .status(ExpenseStatus.PENDING_FINANCE_APPROVAL)
                .expenseDate(dto.getExpenseDate())
                .user(user)
                .build();
    }

    @Override
    @Transactional
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDTO.getUserId()));

        Expense expense = Expense.builder()
                .title(requestDTO.getTitle())
                .amount(BigDecimal.valueOf(requestDTO.getAmount()))
                .category(requestDTO.getCategory())
                .expenseDate(requestDTO.getExpenseDate())
                .user(user)
                .build();

        if (requestDTO.getAttachment() != null && !requestDTO.getAttachment().isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(requestDTO.getAttachment());
                expense.setAttachmentUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        // Check if the amount is below or equal to the auto-approval threshold
        if (expense.getAmount().compareTo(AUTO_APPROVAL_THRESHOLD) <= 0) {
            // If it is, approve it immediately
            expense.setStatus(ExpenseStatus.APPROVED);
            expense.setCurrentApprover(null); // No approver needed
        } else {
            // If it's over the threshold, it needs manager approval
            User manager = user.getManager();
            if (manager == null) {
                // This is a business rule decision: what happens if a user has no manager?
                // For now, we'll throw an error. In a real app, this might go to a default admin.
                throw new IllegalStateException("User does not have a manager assigned. Cannot submit expense for approval.");
            }
            expense.setStatus(ExpenseStatus.PENDING_MANAGER_APPROVAL);
            expense.setCurrentApprover(manager); // Assign it to the user's manager
        }

        Expense savedExpense = expenseRepository.save(expense);
        return mapToDTO(savedExpense);
    }

    @Override
    public ExpenseResponseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapToDTO(expense);
    }

    @Override
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ExpenseResponseDTO updateExpenseStatus(Long expenseId, ExpenseUpdateStatusDTO statusDTO) {
        //Get the currently logged-in user from the security context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User approver = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Approver not found in database"));

        // Find the expense that needs to be updated
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + expenseId));

        // SECURITY CHECK: Is this user the designated approver for this expense?
        if (!expense.getCurrentApprover().getId().equals(approver.getId())) {
            throw new SecurityException("You are not the current approver for this expense.");
        }

        // Handle REJECTION
        if ("REJECTED".equalsIgnoreCase(statusDTO.getStatus())) {
            expense.setStatus(ExpenseStatus.REJECTED);
            expense.setCurrentApprover(null); // Workflow ends
            Expense updatedExpense = expenseRepository.save(expense);
            // TODO: Send email notification to user that their expense was rejected.
            return mapToDTO(updatedExpense);
        }

        // Handle APPROVAL
        if ("APPROVED".equalsIgnoreCase(statusDTO.getStatus())) {
            switch (expense.getStatus()) {
                case PENDING_MANAGER_APPROVAL:
                    // A Manager is approving. The next step is Admin approval.
                    // Find a user with the ADMIN role to be the next approver.
                    User adminApprover = userRepository.findAll().stream()
                            .filter(user -> user.getRole() == UserRole.ADMIN)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No Admin user found to assign for final approval."));

                    expense.setStatus(ExpenseStatus.PENDING_ADMIN_APPROVAL);
                    expense.setCurrentApprover(adminApprover);
                    // TODO: Send email notification to user that their expense has passed manager review.
                    break;

                case PENDING_ADMIN_APPROVAL:
                    // An Admin is giving the final approval.
                    expense.setStatus(ExpenseStatus.APPROVED);
                    expense.setCurrentApprover(null); // Workflow ends successfully.
                    // TODO: Send email notification to user that their expense is fully approved.
                    break;

                default:
                    throw new IllegalStateException("Cannot approve an expense that is not in a pending state.");
            }
        }

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToDTO(updatedExpense);
    }

    @Override
    public List<ExpenseResponseDTO> getExpensesForApproval() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User approver = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Approver not found in database"));

        return expenseRepository.findByCurrentApproverId(approver.getId())
                .stream()
                .map(this::mapToDTO) // Reuse your existing mapping function
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
