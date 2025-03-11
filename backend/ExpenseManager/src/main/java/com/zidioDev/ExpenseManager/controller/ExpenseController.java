package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.UserPrincipal;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<ExpenseDTO>> getMyExpenses(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            logger.debug("Fetching expenses for user: {}", userPrincipal.getUsername());
            
            List<ExpenseDTO> expenses = expenseService.getExpensesByUser(userPrincipal.getUser().getId());
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            logger.error("Error fetching expenses", e);
            throw e;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseDTO expenseDTO, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            logger.debug("Creating expense for user: {}", userPrincipal.getUsername());
            
            // Set default values
            expenseDTO.setUserId(userPrincipal.getUser().getId());
            if (expenseDTO.getDate() == null) {
                expenseDTO.setDate(LocalDateTime.now());
            }
            
            ExpenseDTO savedExpense = expenseService.createExpense(expenseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
        } catch (Exception e) {
            logger.error("Error creating expense", e);
            throw e;
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUser(@PathVariable Long userId) {
        try {
            List<ExpenseDTO> expenses = expenseService.getExpensesByUser(userId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            logger.error("Error fetching expenses for user: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ExpenseDTO expense = expenseService.getExpenseById(id);
            
            // Only allow users to view their own expenses unless they are a manager or admin
            if (!userPrincipal.getUser().getRole().equals(Role.MANAGER) && 
                !userPrincipal.getUser().getRole().equals(Role.ADMIN) &&
                !expense.getUserId().equals(userPrincipal.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            logger.error("Error fetching expense: {}", id, e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseDTO expenseDTO, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ExpenseDTO existingExpense = expenseService.getExpenseById(id);
            
            // Only allow users to update their own expenses unless they are a manager or admin
            if (!userPrincipal.getUser().getRole().equals(Role.MANAGER) && 
                !userPrincipal.getUser().getRole().equals(Role.ADMIN) &&
                !existingExpense.getUserId().equals(userPrincipal.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            logger.error("Error updating expense: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ExpenseDTO existingExpense = expenseService.getExpenseById(id);
            
            // Only allow users to delete their own expenses unless they are a manager or admin
            if (!userPrincipal.getUser().getRole().equals(Role.MANAGER) && 
                !userPrincipal.getUser().getRole().equals(Role.ADMIN) &&
                !existingExpense.getUserId().equals(userPrincipal.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            expenseService.deleteExpense(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting expense: {}", id, e);
            throw e;
        }
    }
}

