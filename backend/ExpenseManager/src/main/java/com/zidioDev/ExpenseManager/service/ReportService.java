package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.ReportDTO;

public interface ReportService {
    ReportDTO generateReport(String reportType);
}



