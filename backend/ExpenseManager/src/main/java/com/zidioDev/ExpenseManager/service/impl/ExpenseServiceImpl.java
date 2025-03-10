package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.model.Expense;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.repository.ExpenseRepository;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setStatus(ApprovalStatus.PENDING);
        expense.setDate(LocalDateTime.now());

        expense = expenseRepository.save(expense);
        return mapToDTO(expense);
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

    private ExpenseDTO mapToDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .userId(expense.getUser().getId())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .status(expense.getStatus().name())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}

