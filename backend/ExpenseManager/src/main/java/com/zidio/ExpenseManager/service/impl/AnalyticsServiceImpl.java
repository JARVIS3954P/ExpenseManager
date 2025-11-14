package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import com.zidio.ExpenseManager.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ExpenseRepository expenseRepository;

    @Override
    public List<ExpenseSummaryDTO> getExpenseSummaryByCategory(){
        return expenseRepository.getExpenseSummaryByCategory();
    }
}
