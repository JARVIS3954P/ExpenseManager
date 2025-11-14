package com.zidio.ExpenseManager.repository;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByStatus(ExpenseStatus status);
    List<Expense> findByCurrentApproverId(Long approverId);

    @Query("SELECT new com.zidio.ExpenseManager.dto.ExpenseSummaryDTO(e.category, SUM(e.amount)) " +
            "FROM Expense e GROUP BY e.category ORDER BY SUM(e.amount) DESC")
    List<ExpenseSummaryDTO> getExpenseSummaryByCategory();
}
