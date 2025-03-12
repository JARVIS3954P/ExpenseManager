package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.NotificationDTO;
import com.zidioDev.ExpenseManager.model.Notification;
import com.zidioDev.ExpenseManager.repository.NotificationRepository;
import com.zidioDev.ExpenseManager.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        Notification notification = Notification.builder()
                .recipientId(notificationDTO.getRecipientId())
                .message(notificationDTO.getMessage())
                .read(false)
                .build();

        notification = notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(
            notificationDTO.getRecipientId(),
            "/queue/notifications",
            notification
        );
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}



