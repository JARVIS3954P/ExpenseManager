package com.zidioDev.ExpenseManager.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_attachments")
@EntityListeners(AuditingEntityListener.class)
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @Version
    private Long version;

    @PreRemove
    private void preRemove() {
        if (expense != null) {
            expense.getAttachments().remove(this);
        }
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
        if (expense != null && !expense.getAttachments().contains(this)) {
            expense.getAttachments().add(this);
        }
    }
}



