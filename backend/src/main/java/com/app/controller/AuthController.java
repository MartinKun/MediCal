package com.app.controller;

import com.app.controller.dto.EmailDTO;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.response.RegisterUserResponse;
import com.app.exception.IncompleteFieldsException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import com.app.util.EmailTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    EmailServiceImpl emailService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @RequestBody RegisterUserRequest request
    ) {

        validateUserTypeFields(request);

        RegisterUserResponse response = authService.register(request);

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
}
