package com.app.controller;

import com.app.controller.dto.request.NotificationRequest;
import com.app.controller.dto.response.NotificationResponse;
import com.app.exception.NotificationAccessDeniedException;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.User;
import com.app.service.implementation.NotificationServiceImpl;
import com.app.service.implementation.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    NotificationServiceImpl notificationService;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @GetMapping
    public ResponseEntity<Set<NotificationResponse>> listNotifications(
            @AuthenticationPrincipal String username,
            @RequestParam int itemsByPage,
            @RequestParam int page
    ) {
        User myUser = (User) userDetailService.loadUserByUsername(username);

        Pageable pageable = PageRequest.of(
                page,
                itemsByPage,
                Sort.by("date").descending());

        Set<NotificationResponse> response = notificationService
                .listNotificationsByUserId(myUser.getId(), pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestBody NotificationRequest request
    ) {
        User user = userDetailService.getUserById(request.getUserId());

        NotificationResponse response = notificationService.createNotification(request, user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> updateNotification(
            @AuthenticationPrincipal String username,
            @RequestBody NotificationRequest request,
            @PathVariable Long id
    ) {
        Notification notification = notificationService.getNotificationById(id);

        if (!notification.getUser().getEmail().equals(username) &&
                !(Objects.equals(notification.getUser().getId(), request.getUserId())))
            throw new NotificationAccessDeniedException("You do not have permission to update this appointment.");

        NotificationResponse response = notificationService.updateNotification(request, notification);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(
            @AuthenticationPrincipal String username,
            @PathVariable Long id
    ) {
        Notification notification = notificationService.getNotificationById(id);

        if (!notification.getUser().getEmail().equals(username))
            throw new NotificationAccessDeniedException("You do not have permission to delete this appointment.");

        notificationService.deleteNotification(notification);
        return ResponseEntity.ok("Notification was deleted successfully.");
    }

}
