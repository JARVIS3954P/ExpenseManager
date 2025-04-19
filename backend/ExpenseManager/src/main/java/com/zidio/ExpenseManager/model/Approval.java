package com.zidio.ExpenseManager.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean approved;
    private String rejectionReason;

    private LocalDateTime approvedAt;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @OneToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;
}
