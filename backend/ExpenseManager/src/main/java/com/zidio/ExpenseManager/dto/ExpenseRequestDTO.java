package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequestDTO {
    private String title;
    private String description; // Optional — not in Expense entity
    private Double amount;
    private Long userId;
    private ExpenseCategory category;
    private LocalDate expenseDate;
    private MultipartFile attachment; // Optional
}

