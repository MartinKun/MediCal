package com.app.service;

import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.User;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request, User user);

}
