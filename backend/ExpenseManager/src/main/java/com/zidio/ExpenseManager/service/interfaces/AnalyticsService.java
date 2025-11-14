package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.dto.TeamMemberExpenseDTO;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    List<ExpenseSummaryDTO> getExpenseSummaryByCategory();

    Map<String, Object> getDashboardSummary();

    List<TeamMemberExpenseDTO> getTeamExpenseSummary();
}

