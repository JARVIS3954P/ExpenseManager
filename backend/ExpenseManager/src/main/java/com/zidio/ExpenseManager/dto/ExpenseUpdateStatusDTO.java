package com.zidio.ExpenseManager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseUpdateStatusDTO {
    private String status; // APPROVED or REJECTED
    private String rejectionReason; // Reason for rejection
    private String remarks; // Remarks from the approver
}
