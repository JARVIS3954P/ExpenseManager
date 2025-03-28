package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/approve/{expenseId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApprovalDTO> approveExpense(
            @PathVariable Long expenseId,
            @RequestParam String approverRole) {
        ApprovalDTO approvalDTO = ApprovalDTO.builder()
                .expenseId(expenseId)
                .approverRole(approverRole)
                .approved(true)
                .build();
        return ResponseEntity.ok(approvalService.approveExpense(approvalDTO));
    }

    @PostMapping("/reject/{expenseId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApprovalDTO> rejectExpense(
            @PathVariable Long expenseId,
            @RequestParam String approverRole,
            @RequestParam String rejectionReason) {
        ApprovalDTO approvalDTO = ApprovalDTO.builder()
                .expenseId(expenseId)
                .approverRole(approverRole)
                .approved(false)
                .rejectionReason(rejectionReason)
                .build();
        return ResponseEntity.ok(approvalService.rejectExpense(approvalDTO));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<ApprovalDTO>> getPendingApprovals(@RequestParam String role) {
        return ResponseEntity.ok(approvalService.getPendingApprovals(role));
    }
}



