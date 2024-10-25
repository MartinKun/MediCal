package com.app.exception;

public class UserDoesNotExistException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "User does not exist.";

    public UserDoesNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public UserDoesNotExistException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
