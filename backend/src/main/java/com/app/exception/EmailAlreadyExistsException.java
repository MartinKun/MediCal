package com.app.exception;

public class EmailAlreadyExistsException extends ConflictException {
    private static final String DEFAULT_MESSAGE = "Email already exists.";

    public EmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyExistsException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
