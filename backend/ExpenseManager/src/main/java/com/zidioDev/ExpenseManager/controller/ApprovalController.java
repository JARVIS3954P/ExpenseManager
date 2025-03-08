package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/{expenseId}/approve")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<String> approveExpense(@PathVariable Long expenseId) {
        approvalService.approveExpense(expenseId);
        return ResponseEntity.ok("Expense approved successfully");
    }

    @PostMapping("/{expenseId}/reject")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<String> rejectExpense(@PathVariable Long expenseId, @RequestParam String reason) {
        approvalService.rejectExpense(expenseId, reason);
        return ResponseEntity.ok("Expense rejected successfully");
    }
}

