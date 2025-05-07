package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Category is required")
    private ExpenseCategory category;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    private MultipartFile attachment; // Optional
}


