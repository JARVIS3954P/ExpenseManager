package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.NotificationDTO;
import com.zidioDev.ExpenseManager.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok().build();
    }
}



