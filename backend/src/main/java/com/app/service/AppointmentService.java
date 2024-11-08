package com.app.service;

import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.User;

import java.util.Set;

public interface AppointmentService {

    Set<AppointmentResponse> listAppointmentsByUserAndMonth(User user, int month, int year);

    AppointmentResponse createAppointment(Appointment appointment);

    Appointment getAppointmentById(Long id);

    void deleteAppointment(Appointment appointment);

}
