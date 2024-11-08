package com.app.exception;

public class InvalidYearException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "Invalid year";

    public InvalidYearException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidYearException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
