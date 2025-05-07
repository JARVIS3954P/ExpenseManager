package com.zidio.ExpenseManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseUpdateStatusDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(APPROVED|REJECTED)$", message = "Status must be either APPROVED or REJECTED")
    private String status;

    // Required only if REJECTED
    private String rejectionReason;

    private String remarks;
}

