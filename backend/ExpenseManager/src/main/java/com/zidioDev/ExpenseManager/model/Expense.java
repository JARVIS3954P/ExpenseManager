package com.zidioDev.ExpenseManager.model;

import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
import com.zidioDev.ExpenseManager.model.enums.ExpenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses", indexes = {
    @Index(name = "idx_expense_date", columnList = "date"),
    @Index(name = "idx_expense_status", columnList = "status"),
    @Index(name = "idx_expense_user", columnList = "user_id")
})
@EntityListeners(AuditingEntityListener.class)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Expense date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime date;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User reviewer;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private Long version;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FileAttachment> attachments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAttachment(FileAttachment attachment) {
        attachments.add(attachment);
        attachment.setExpense(this);
    }

    public void removeAttachment(FileAttachment attachment) {
        attachments.remove(attachment);
        attachment.setExpense(null);
    }
}



