package com.zidioDev.ExpenseManager.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String reportType;  // PDF or EXCEL
    private LocalDateTime generatedAt;
    private String downloadUrl;
} 