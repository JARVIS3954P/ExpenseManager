package com.zidio.ExpenseManager.controller;

import com.zidio.ExpenseManager.model.Approval;
import com.zidio.ExpenseManager.service.interfaces.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/approve/{expenseId}")
    public ResponseEntity<Approval> approveExpense(
            @PathVariable Long expenseId,
            @RequestParam Long approverId,
            @RequestParam(required = false) String remarks) {

        Approval approval = approvalService.approveExpense(expenseId, approverId, remarks);
        return new ResponseEntity<>(approval, HttpStatus.OK);
    }

    @PostMapping("/reject/{expenseId}")
    public ResponseEntity<Approval> rejectExpense(
            @PathVariable Long expenseId,
            @RequestParam Long approverId,
            @RequestParam String rejectionReason,
            @RequestParam(required = false) String remarks) {

        Approval approval = approvalService.rejectExpense(expenseId, approverId, rejectionReason, remarks);
        return new ResponseEntity<>(approval, HttpStatus.OK);
    }
}