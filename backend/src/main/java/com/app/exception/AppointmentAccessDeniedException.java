package com.app.exception;

public class AppointmentAccessDeniedException extends ForbiddenException{

    private static final String DEFAULT_MESSAGE = "You do not have permission to delete this appointment.";

    public AppointmentAccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public AppointmentAccessDeniedException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
