package com.app.exception;

public class UserNotEnabledException extends ForbiddenException {
    private static final String DEFAULT_MESSAGE = "User is not enabled.";
    public UserNotEnabledException() {
        super(DEFAULT_MESSAGE);;
    }
    public UserNotEnabledException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
