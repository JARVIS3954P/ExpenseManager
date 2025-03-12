package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.ReportDTO;
import com.zidioDev.ExpenseManager.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{reportType}")
    public ResponseEntity<ReportDTO> generateReport(@PathVariable String reportType) {
        ReportDTO report = reportService.generateReport(reportType);
        return ResponseEntity.ok(report);
    }
}



