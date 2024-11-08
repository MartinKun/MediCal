package com.app.controller;


import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.UnauthorizedAppointmentCreationException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.service.implementation.AppointmentServiceImpl;
import com.app.service.implementation.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    AppointmentServiceImpl appointmentService;

    @GetMapping
    public ResponseEntity<Set<AppointmentResponse>> listAppointmentByMonth(
            @AuthenticationPrincipal String username,
            @RequestParam int month,
            @RequestParam int year
    ) {
        User myUser = (User) userDetailService.loadUserByUsername(username);

        Set<AppointmentResponse> appointments = appointmentService.listAppointmentsByUserAndMonth(myUser, month, year);

        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @AuthenticationPrincipal String username,
            @RequestBody AppointmentRequest request
    ) {
        User myUser = (User) userDetailService.loadUserByUsername(username);
        User myPatient = (User) userDetailService.loadUserByUsername(request.getPatientEmail());

        if (!(myUser instanceof Doctor doctor))
            throw new UnauthorizedAppointmentCreationException("Only doctors can create appointments.");

        if (!(myPatient instanceof Patient patient))
            throw new UnauthorizedAppointmentCreationException(
                    String.format("User with email %s is not a Patient", request.getPatientEmail()));


        Appointment appointment = Appointment.builder()
                .address(request.getAddress())
                .date(request.getDate())
                .reason(request.getReason())
                .doctor(doctor)
                .patient(patient)
                .build();

        AppointmentResponse response = appointmentService.createAppointment(appointment);

        return ResponseEntity.ok(response);
    }


}
