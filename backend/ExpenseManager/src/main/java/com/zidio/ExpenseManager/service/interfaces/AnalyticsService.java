package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;

import java.util.List;

public interface AnalyticsService {
    List<ExpenseSummaryDTO> getExpenseSummaryByCategory();
}
