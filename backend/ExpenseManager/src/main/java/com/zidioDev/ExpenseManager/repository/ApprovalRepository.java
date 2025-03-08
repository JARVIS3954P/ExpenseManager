package com.zidioDev.ExpenseManager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByApproverId(Long approverId);  // Fetch approvals assigned to a specific manager

    @Query("SELECT a FROM Approval a WHERE a.expense.id = :expenseId")
    Optional<Approval> findByExpenseId(@Param("expenseId") Long expenseId);
}

