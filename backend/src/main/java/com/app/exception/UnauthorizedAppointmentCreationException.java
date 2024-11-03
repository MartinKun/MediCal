package com.app.exception;

public class UnauthorizedAppointmentCreationException extends ForbiddenException {
    private static final String DEFAULT_MESSAGE = "You do not have permission to create an appointment.";

    public UnauthorizedAppointmentCreationException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedAppointmentCreationException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}