package com.zidioDev.ExpenseManager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String category;  // e.g., Travel, Food, Office Supplies

    @Positive
    private Double amount;

    @NotBlank
    private String title;

    private String status; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

