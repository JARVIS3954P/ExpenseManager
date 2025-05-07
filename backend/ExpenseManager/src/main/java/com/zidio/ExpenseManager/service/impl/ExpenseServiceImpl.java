package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.ExpenseRequestDTO;
import com.zidio.ExpenseManager.dto.ExpenseResponseDTO;
import com.zidio.ExpenseManager.dto.ExpenseUpdateStatusDTO;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import com.zidio.ExpenseManager.model.Approval;
import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.service.interfaces.ApprovalService;
import com.zidio.ExpenseManager.service.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ApprovalService approvalService;

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
                .status(ExpenseStatus.PENDING)
                .expenseDate(dto.getExpenseDate())
                .user(user)
                .build();
    }

    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {
        Expense expense = expenseRepository.save(mapToEntity(requestDTO));
        return mapToDTO(expense);
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
    public ExpenseResponseDTO updateExpenseStatus(Long expenseId, ExpenseUpdateStatusDTO statusDTO) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if ("APPROVED".equalsIgnoreCase(statusDTO.getStatus())) {
            // Approve the expense
            Approval approval = approvalService.approveExpense(expenseId, 1L, statusDTO.getRemarks());
            expense.setStatus(ExpenseStatus.APPROVED); // Set the status to APPROVED
        } else if ("REJECTED".equalsIgnoreCase(statusDTO.getStatus())) {
            // Reject the expense
            Approval approval = approvalService.rejectExpense(expenseId, 1L, statusDTO.getRejectionReason(), statusDTO.getRemarks());
            expense.setStatus(ExpenseStatus.REJECTED); // Set the status to REJECTED
        }
        Expense updatedExpense = expenseRepository.save(expense);
        return mapToDTO(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
