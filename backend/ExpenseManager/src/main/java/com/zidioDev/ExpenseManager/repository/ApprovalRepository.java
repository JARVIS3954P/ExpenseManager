package com.zidioDev.ExpenseManager.repository;

import com.zidioDev.ExpenseManager.model.Approval;
import com.zidioDev.ExpenseManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByReviewer(User reviewer);
    Optional<Approval> findByExpenseId(Long expenseId);
}



