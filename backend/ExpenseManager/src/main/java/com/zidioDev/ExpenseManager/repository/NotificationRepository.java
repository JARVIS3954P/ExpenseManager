package com.zidioDev.ExpenseManager.repository;

import com.zidioDev.ExpenseManager.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndSeenFalse(Long userId);  // Fetch unread notifications

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.seen = TRUE WHERE n.userId = :userId")
    void markNotificationsAsRead(@Param("userId") Long userId);
}

