package com.app.exception;

public class UserAlreadyEnabledException extends ConflictException {
    private static final String DEFAULT_MESSAGE = "User has already been enabled.";

    public UserAlreadyEnabledException() {
        super(DEFAULT_MESSAGE);
    }

    public UserAlreadyEnabledException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
