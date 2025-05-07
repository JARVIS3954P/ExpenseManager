package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseCategory;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private ExpenseStatus status;
    private ExpenseCategory category;
    private Long userId;
    private String attachmentUrl;
    private LocalDate expenseDate;
}
