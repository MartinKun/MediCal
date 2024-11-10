package com.app.exception;

public class NotificationAccessDeniedException extends ForbiddenException{

    private static final String DEFAULT_MESSAGE = "You do not have permission to access this notification.";

    public NotificationAccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public NotificationAccessDeniedException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
