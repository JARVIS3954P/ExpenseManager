package com.zidio.ExpenseManager.repository;

import com.zidio.ExpenseManager.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}
