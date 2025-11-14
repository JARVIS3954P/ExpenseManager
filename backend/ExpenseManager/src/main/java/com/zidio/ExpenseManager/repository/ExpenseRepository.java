package com.zidio.ExpenseManager.repository;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.dto.TeamMemberExpenseDTO;
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

    @Query("SELECT count(e) FROM Expense e WHERE e.status IN :statuses")
    long countByStatusIn(List<ExpenseStatus> statuses);

    @Query("SELECT new com.zidio.ExpenseManager.dto.TeamMemberExpenseDTO(u.id, u.fullName, SUM(e.amount)) " +
            "FROM Expense e JOIN e.user u " +
            "WHERE u.manager.id = :managerId " +
            "GROUP BY u.id, u.fullName")
    List<TeamMemberExpenseDTO> getTeamExpenseSummary(Long managerId);

    @Query("SELECT e FROM Expense e WHERE e.user.id IN :userIds")
    List<Expense> findByUserIdIn(List<Long> userIds);
}
