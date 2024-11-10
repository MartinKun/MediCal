package com.app.service.implementation;

import com.app.common.util.DateUtils;
import com.app.controller.dto.request.NotificationRequest;
import com.app.controller.dto.response.NotificationResponse;
import com.app.exception.NotificationNotFoundException;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.User;
import com.app.persistence.repository.NotificationRepository;
import com.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Set<NotificationResponse> listNotificationsByUserId(Long userId, Pageable pageable) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId, pageable)
                .getContent();

        return notifications.stream()
                .map(this::buildNotificationResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request, User user) {
        Notification notification = Notification.builder()
                .date(request.getDate())
                .type(request.getType())
                .content(request.getContent())
                .isRead(request.isRead())
                .user(user)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        return buildNotificationResponse(savedNotification);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(
                        String.format("Notification with ID %d does not exist.", id)
                ));
    }

    @Override
    public NotificationResponse updateNotification(NotificationRequest request, Notification notification) {
        notification.setDate(request.getDate());
        notification.setType(request.getType());
        notification.setContent(request.getContent());
        notification.setRead(request.isRead());

        Notification savedNotification = notificationRepository.save(notification);

        return buildNotificationResponse(savedNotification);
    }

    @Override
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public NotificationResponse buildNotificationResponse(
            Notification notification
    ) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .date(DateUtils.formatDate(notification.getDate()))
                .type(notification.getType())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .userId(notification.getUser().getId())
                .build();
    }
}
