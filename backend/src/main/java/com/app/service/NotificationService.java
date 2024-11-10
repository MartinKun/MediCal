package com.app.service;

import com.app.controller.dto.request.NotificationRequest;
import com.app.controller.dto.response.NotificationResponse;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface NotificationService {

    Set<NotificationResponse> listNotificationsByUserId(Long userId, Pageable pageable);

    NotificationResponse createNotification(NotificationRequest request, User user);

    Notification getNotificationById(Long id);

    NotificationResponse updateNotification(NotificationRequest request, Notification notification);

    void deleteNotification(Notification notification);
}
