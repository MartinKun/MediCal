package com.app.exception;

public class InvalidMonthException extends BadRequestException{

    private static final String DEFAULT_MESSAGE = "Invalid month. Please provide a value between 1 and 12.";

    public InvalidMonthException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidMonthException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
