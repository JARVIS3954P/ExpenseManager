package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.ApprovalDTO;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/{expenseId}/approve")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApprovalDTO> approveExpense(@PathVariable Long expenseId, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        ApprovalDTO approval = approvalService.approveExpense(expenseId, role);
        return ResponseEntity.ok(approval);
    }

    @PostMapping("/{expenseId}/reject")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApprovalDTO> rejectExpense(
            @PathVariable Long expenseId,
            @RequestParam String reason,
            Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        ApprovalDTO approval = approvalService.rejectExpense(expenseId, role, reason);
        return ResponseEntity.ok(approval);
    }
}

