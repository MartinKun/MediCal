package com.app.exception;

public class NotificationNotFoundException extends NotFoundException{

    private static final String DEFAULT_MESSAGE = "Notification does not exist.";

    public NotificationNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public NotificationNotFoundException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
