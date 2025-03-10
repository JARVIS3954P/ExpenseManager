package com.zidioDev.ExpenseManager.dto;

import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.model.enums.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    @NotNull(message = "Category cannot be null")
    private ExpenseCategory category;

    private Long userId;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String status;

    private Long reviewerId;

    @Size(max = 500, message = "Rejection reason cannot exceed 500 characters")
    private String rejectionReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

