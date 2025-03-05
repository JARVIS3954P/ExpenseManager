package com.zidioDev.ExpenseManager.model;

import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_workflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;  // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;  // Manager or Admin

    @Column(nullable = false)
    private LocalDateTime approvalDate;
}

