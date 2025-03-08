package com.zidioDev.ExpenseManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;

    @NotNull
    private Long userId;

    private String message;

    private Boolean readStatus;

    private LocalDateTime timestamp;
}



