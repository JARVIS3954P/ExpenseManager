package com.zidio.ExpenseManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TeamMemberExpenseDTO {
    private Long userId;
    private String fullName;
    private BigDecimal totalAmount;
}