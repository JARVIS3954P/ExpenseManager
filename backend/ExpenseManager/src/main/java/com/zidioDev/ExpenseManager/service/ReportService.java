package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.expense.ReportDTO;

public interface ReportService {
    ReportDTO generateReport(String reportType);
}



