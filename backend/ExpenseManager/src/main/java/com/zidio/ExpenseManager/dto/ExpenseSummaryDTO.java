package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpenseSummaryDTO {
    private String category;
    private BigDecimal totalAmount;

    public ExpenseSummaryDTO(ExpenseCategory category, Number totalAmount) {
        this.category = category.name(); // Convert the enum to its string name
        // Handle different possible numeric types from the SUM function
        if (totalAmount != null) {
            this.totalAmount = new BigDecimal(totalAmount.toString());
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
    }
}