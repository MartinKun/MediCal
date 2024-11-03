package com.app.controller;


import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.User;
import com.app.service.implementation.AppointmentServiceImpl;
import com.app.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AppointmentServiceImpl appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @AuthenticationPrincipal String username,
            @RequestBody AppointmentRequest request
    ){
        User user = userService.getUserByEmail(username);

        AppointmentResponse response = appointmentService.createAppointment(request, user);

        return ResponseEntity.ok(response);
    }

}
