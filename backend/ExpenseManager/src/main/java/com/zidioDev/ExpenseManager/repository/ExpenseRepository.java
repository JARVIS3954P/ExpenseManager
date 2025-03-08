package com.zidioDev.ExpenseManager.repository;

import com.zidioDev.ExpenseManager.model.Expense;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);  // Fetch expenses for a specific user

    @Query("SELECT e FROM Expense e WHERE e.status = 'PENDING' ORDER BY e.createdAt DESC")
    List<Expense> findAllPendingExpenses();

    @Modifying
    @Transactional
    @Query("UPDATE Expense e SET e.status = :status WHERE e.id = :expenseId")
    void updateExpenseStatus(@Param("expenseId") Long expenseId, @Param("status") String status);
}
