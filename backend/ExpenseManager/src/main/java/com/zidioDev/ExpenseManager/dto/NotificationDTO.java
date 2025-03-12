package com.zidioDev.ExpenseManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String recipientId;
    private String message;
    private String type;
    private LocalDateTime timestamp;
    private boolean read;
}





