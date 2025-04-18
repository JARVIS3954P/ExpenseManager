package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double amount;
    private ExpenseStatus status;
    private CategoryDTO category;
    private UserDTO createdBy;
    private AttachmentDTO attachment;
    private LocalDateTime createdAt;
}
