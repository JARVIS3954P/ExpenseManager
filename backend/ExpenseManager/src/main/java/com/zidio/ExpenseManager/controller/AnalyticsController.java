package com.zidio.ExpenseManager.controller;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary/by-category")
    @PreAuthorize("hasRole('ADMIN')") // Only Admins can access analytics
    public ResponseEntity<List<ExpenseSummaryDTO>> getSummaryByCategory() {
        return ResponseEntity.ok(analyticsService.getExpenseSummaryByCategory());
    }
}