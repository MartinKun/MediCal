package com.app.controller.dto.response;

import com.app.common.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String date;
    private NotificationType type;
    private String content;
    private boolean isRead;
    private Long userId;
}
