package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.NotificationDTO;
import com.zidioDev.ExpenseManager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate; // WebSocket support

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSend("/topic/notifications", notificationDTO);
    }
}

