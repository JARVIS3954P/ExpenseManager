package com.zidioDev.ExpenseManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDTO {
    private Long expenseId;

    @NotBlank
    private String approverRole;  // MANAGER, FINANCE, ADMIN

    private Boolean approved;

    private String rejectionReason;

    private LocalDateTime approvedAt;
}
