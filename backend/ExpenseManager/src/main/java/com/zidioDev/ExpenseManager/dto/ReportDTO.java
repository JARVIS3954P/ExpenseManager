package com.zidioDev.ExpenseManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String reportType;  // PDF or EXCEL
    private LocalDateTime generatedAt;
    private String downloadUrl;
}
