package com.zidioDev.ExpenseManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who performed the action

    @Column(nullable = false)
    private String action;  // "CREATED_EXPENSE", "APPROVED_EXPENSE", etc.

    @Column(nullable = false)
    private LocalDateTime timestamp;
}



