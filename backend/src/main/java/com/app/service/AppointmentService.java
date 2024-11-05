package com.app.service;

import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.Appointment;

public interface AppointmentService {

    AppointmentResponse createAppointment(Appointment appointment);

}
