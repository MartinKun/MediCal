package com.app.controller;

import com.app.controller.dto.request.ConfirmUserRequest;
import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.ForgotPassRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.exception.IncompleteFieldsException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(
            @RequestBody RegisterUserRequest request
    ) {
        validateUserTypeFields(request);

        UserRegistrationResponse response = authService.register(request);

        String token = authService.generateConfirmToken(request.getEmail());

        emailService.sendConfirmUserEmail(
                response.getFirstName(),
                response.getEmail(),
                token);

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

    @PutMapping("/confirm")
    public ResponseEntity<UserRegistrationResponse> confirmUser(
            @RequestBody ConfirmUserRequest request
    ) {
        String token = request.getToken();

        UserRegistrationResponse response = authService.confirmUser(token);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPassRequest request
            ){
        String email = request.getEmail();

        if(authService.emailExists(email)){
            String token = authService.generatePasswordResetToken(email);
            emailService.sendResetPassEmail(email, token);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("If the email exists, a password reset email has been sent.");
    }

}
