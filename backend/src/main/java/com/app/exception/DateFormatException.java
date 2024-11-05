package com.app.exception;

public class DateFormatException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "Incorrect date format.";

    public DateFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public DateFormatException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
