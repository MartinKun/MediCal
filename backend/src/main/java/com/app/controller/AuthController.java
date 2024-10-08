package com.app.controller;

import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.RecoveryPassRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.exception.IncompleteFieldsException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    EmailServiceImpl emailService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> signup(
            @RequestBody RegisterUserRequest request
    ) {

        validateUserTypeFields(request);

        UserRegistrationResponse response = authService.signup(request);

        emailService.sendConfirmUserEmail(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void validateUserTypeFields(RegisterUserRequest request) {
        if (request.getRole() == RoleEnum.DOCTOR) {
            if (request.getOfficeAddress() == null &&
                    request.getSpeciality() == null &&
                    request.getLicense() == null) {
                throw new IncompleteFieldsException("Incomplete fields for Doctor");
            }
        }

        if (request.getRole() == RoleEnum.PATIENT) {
            if (request.getAddress() == null) {
                throw new IncompleteFieldsException("Incomplete fields for Patient");
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ){
        LoginResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping("/confirmUser")
    public ResponseEntity<UserRegistrationResponse> confirmUser(
            HttpServletRequest request
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }

        String token = authHeader.substring(7);

        UserRegistrationResponse response = authService.confirmUser(token);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/recoveryPassword")
    public ResponseEntity<String> recoveryPassword(
            @RequestBody RecoveryPassRequest request
            ){
        String newPassword = authService.recoveryPassword(request);

        emailService.sendRecoveryPassEmail(request.getEmail(), newPassword);

        return ResponseEntity.status(HttpStatus.OK).body("An email was sent with a new password.");

    }

}
