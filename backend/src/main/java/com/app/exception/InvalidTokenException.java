package com.app.exception;

public class InvalidTokenException extends UnauthorizedException{
    private static final String DEFAULT_MESSAGE = "Token invalid, not Authorized";

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidTokenException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
