package com.zidio.ExpenseManager.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequestDTO {
    private String title;
    private String description;
    private Double amount;
    private Long categoryId;
    private MultipartFile attachment; // Optional
}
