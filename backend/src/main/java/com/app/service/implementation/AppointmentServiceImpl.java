package com.app.service.implementation;

import com.app.common.util.DateUtils;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.Appointment;
import com.app.persistence.repository.AppointmentRepository;
import com.app.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public AppointmentResponse createAppointment(Appointment appointment) {

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return AppointmentResponse.builder()
                .participant(AppointmentResponse
                        .ParticipantInfo
                        .builder()
                        .fullName(String.format(
                                "%s %s",
                                savedAppointment.getPatient().getFirstName(),
                                savedAppointment.getPatient().getLastName()
                        ))
                        .avatar(appointment.getPatient().getImage())
                        .build())
                .address(savedAppointment.getAddress())
                .date(DateUtils.formatDate(savedAppointment.getDate()))
                .reason(savedAppointment.getReason())
                .build();
    }
}
