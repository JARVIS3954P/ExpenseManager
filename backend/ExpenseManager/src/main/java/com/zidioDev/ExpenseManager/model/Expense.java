package com.zidioDev.ExpenseManager.model;

import com.zidioDev.ExpenseManager.model.Enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.model.Enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;  // E.g., "Client Meeting Lunch"

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category; // ENUM (TRAVEL, FOOD, etc.)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Expense belongs to a User

    @OneToOne(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private FileAttachment invoice; // File Attachment (PDF/Image)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;  // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;  // The Manager/Admin who approved the expense
}

