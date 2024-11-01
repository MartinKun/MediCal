package com.app.controller;

import com.app.controller.dto.request.*;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.exception.EmailAlreadyExistsException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import jakarta.validation.Valid;
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
            @Valid @RequestBody RegisterUserRequest request
    ) {
        if(authService.emailExists(request.getEmail()))
            throw new EmailAlreadyExistsException(String.format("The email %s is already registered.",request.getEmail()));

        UserRegistrationResponse response = authService.register(request);

        String token = authService.generateConfirmToken(request.getEmail());

        emailService.sendConfirmUserEmail(
                response.getFirstName(),
                response.getEmail(),
                token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
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
            @Valid @RequestBody ForgotPassRequest request
            ){
        String email = request.getEmail();

        if(authService.emailExists(email)){
            String token = authService.generatePasswordResetToken(email);
            emailService.sendResetPassEmail(email, token);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("If the email exists, a password reset email has been sent.");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPassRequest request
            ){
        authService.resetPassword(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Password reset successfully");
    }

}
