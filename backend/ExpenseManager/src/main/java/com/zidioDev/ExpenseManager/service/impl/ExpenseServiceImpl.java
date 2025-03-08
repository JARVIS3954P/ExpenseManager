package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.model.Expense;
import com.zidioDev.ExpenseManager.repository.ExpenseRepository;
import com.zidioDev.ExpenseManager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        expense.setId(expenseDTO.getUserId());
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setTitle(expenseDTO.getTitle());
        expense.setStatus("PENDING");

        expenseRepository.save(expense);
        return new ExpenseDTO(expense.getId(), expense.getUserId(), expense.getCategory(),
                expense.getAmount(), expense.getDescription(), expense.getStatus(),
                expense.getCreatedAt(), expense.getUpdatedAt());
    }

    @Override
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId)
                .stream()
                .map(expense -> new ExpenseDTO(expense.getId(), expense.getUserId(), expense.getCategory(),
                        expense.getAmount(), expense.getDescription(), expense.getStatus(),
                        expense.getCreatedAt(), expense.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }
}

