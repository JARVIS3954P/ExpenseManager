package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.ReportDTO;
import com.zidioDev.ExpenseManager.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public ReportDTO generateReport(String reportType) {
        // Simulating report generation
        String downloadUrl = "https://example.com/reports/" + UUID.randomUUID() + "." + reportType.toLowerCase();
        return new ReportDTO(reportType, LocalDateTime.now(), downloadUrl);
    }
}

