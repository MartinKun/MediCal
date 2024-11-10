package com.app.controller.dto.request;

import com.app.common.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private LocalDateTime date;
    private NotificationType type;
    private String content;
    private boolean isRead;
    private Long userId;
}
