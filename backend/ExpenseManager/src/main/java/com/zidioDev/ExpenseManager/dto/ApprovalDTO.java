package com.zidioDev.ExpenseManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
