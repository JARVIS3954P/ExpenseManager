package com.zidio.ExpenseManager.controller;

import com.zidio.ExpenseManager.dto.ExpenseRequestDTO;
import com.zidio.ExpenseManager.dto.ExpenseResponseDTO;
import com.zidio.ExpenseManager.dto.ExpenseUpdateStatusDTO;
import com.zidio.ExpenseManager.service.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // Create a new expense
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO requestDTO) {
        ExpenseResponseDTO createdExpense = expenseService.createExpense(requestDTO);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    // Get expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) {
        ExpenseResponseDTO expense = expenseService.getExpenseById(id);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    // Get all expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() {
        List<ExpenseResponseDTO> expenses = expenseService.getAllExpenses();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Update the status of an expense (approve/reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<ExpenseResponseDTO> updateExpenseStatus(
            @PathVariable Long id,
            @RequestBody ExpenseUpdateStatusDTO statusDTO) {

        ExpenseResponseDTO updatedExpense = expenseService.updateExpenseStatus(id, statusDTO);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    // Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/for-approval")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesForApproval() {
        List<ExpenseResponseDTO> expenses = expenseService.getExpensesForApproval();
        return ResponseEntity.ok(expenses);
    }
}
