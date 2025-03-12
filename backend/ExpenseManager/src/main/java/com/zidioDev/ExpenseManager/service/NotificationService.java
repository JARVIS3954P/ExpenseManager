package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.NotificationDTO;

public interface NotificationService {
    void sendNotification(NotificationDTO notificationDTO);
    void markAsRead(Long notificationId);
}


