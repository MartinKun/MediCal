package com.app.controller;


import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.AppointmentAccessDeniedException;
import com.app.exception.InvalidMonthException;
import com.app.exception.InvalidYearException;
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

import java.time.LocalDateTime;
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
        if (month < 1 || month > 12)
            throw new InvalidMonthException();

        int currentYear = LocalDateTime.now().getYear();
        if (year < 2000 || year > currentYear + 10)
            throw new InvalidYearException(
                    String.format("Invalid year. Please provide a value between 2000 and %d.", currentYear + 10));

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointmentById(
            @AuthenticationPrincipal String username,
            @PathVariable Long id
    ) {

        Appointment appointment = appointmentService.getAppointmentById(id);

        if(!appointment.getDoctor().getEmail().equals(username)
        && !appointment.getPatient().getEmail().equals(username))
            throw new AppointmentAccessDeniedException("You do not have permission to delete this appointment.");

        appointmentService.deleteAppointment(appointment);

        return ResponseEntity.ok("Appointment was deleted successfully.");
    }

}
