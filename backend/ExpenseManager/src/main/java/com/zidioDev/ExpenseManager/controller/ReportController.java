package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/expenses/pdf")
    public ResponseEntity<Resource> downloadExpenseReportPDF() {
        Resource pdfReport = reportService.generateExpenseReportPDF();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense_report.pdf")
                .body(pdfReport);
    }

    @GetMapping("/expenses/excel")
    public ResponseEntity<Resource> downloadExpenseReportExcel() {
        Resource excelReport = reportService.generateExpenseReportExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense_report.xlsx")
                .body(excelReport);
    }
}

