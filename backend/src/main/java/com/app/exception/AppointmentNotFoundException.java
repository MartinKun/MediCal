package com.app.exception;

public class AppointmentNotFoundException extends NotFoundException{

    private static final String DEFAULT_MESSAGE = "Appointment does not exist";

    public AppointmentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public AppointmentNotFoundException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }
}
