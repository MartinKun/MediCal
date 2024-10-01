package com.app.controller;

import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.response.RegisterUserResponse;
import com.app.service.implementation.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @RequestBody RegisterUserRequest request
    ) {

        validateUserTypeFields(request);

        RegisterUserResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    private void validateUserTypeFields(RegisterUserRequest request) {
        if (request.getRole() == RoleEnum.DOCTOR) {
            if (request.getOfficeAddress() == null &&
                    request.getSpeciality() == null &&
                    request.getLicense() == null) {
                throw new RuntimeException("Incomplete fields for Doctor");
            }
        }

        if (request.getRole() == RoleEnum.PATIENT) {
            if (request.getAddress() == null) {
                throw new RuntimeException("Incomplete fields for Patient");
            }
        }
    }
}
