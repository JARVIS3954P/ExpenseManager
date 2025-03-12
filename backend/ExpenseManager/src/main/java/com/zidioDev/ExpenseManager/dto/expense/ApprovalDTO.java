package com.zidioDev.ExpenseManager.dto.expense;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDTO {
    private Long expenseId;

    @NotBlank
    private String approverRole;  // MANAGER, FINANCE, ADMIN

    private Boolean approved;

    private String rejectionReason;

    private LocalDateTime approvedAt;
} 