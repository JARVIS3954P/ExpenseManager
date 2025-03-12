package com.zidioDev.ExpenseManager.repository;

import com.zidioDev.ExpenseManager.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getExpenseBreakdownByCategory();

    @Query("SELECT MONTH(e.createdAt), SUM(e.amount) FROM Expense e WHERE YEAR(e.createdAt) = :year GROUP BY MONTH(e.createdAt)")
    List<Object[]> getMonthlyExpenses(@Param("year") int year);
}



