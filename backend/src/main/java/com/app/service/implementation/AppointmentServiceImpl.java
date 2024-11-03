package com.app.service.implementation;

import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.UnauthorizedAppointmentCreationException;
import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.AppointmentRepository;
import com.app.persistence.repository.UserRepository;
import com.app.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request, User user) {
        if(user instanceof Patient)
            throw new UnauthorizedAppointmentCreationException();

        Doctor doctor = (Doctor) user;

        Patient patient = (Patient) userRepository.findUserByEmail(request.getPatientEmail())
                .orElseThrow(() -> new UserDoesNotExistException("Patient does not exist"));

        Appointment appointment = Appointment.builder()
                .date(request.getDate())
                .reason(request.getReason())
                .address(request.getAddress())
                .doctor(doctor)
                .patient(patient)
                .build();
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return AppointmentResponse.builder()
                .userName(
                        String.format(
                                "%s %s",
                                savedAppointment.getPatient().getFirstName(),
                                savedAppointment.getPatient().getLastName()
                        ))
                .userAvatar(appointment.getPatient().getImage())
                .address(savedAppointment.getAddress())
                .date(savedAppointment.getDate())
                .reason(savedAppointment.getReason())
                .build();
    }
}