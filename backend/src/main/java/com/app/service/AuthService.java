package com.app.service;

import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.RecoveryPassRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.UserRegistrationResponse;

public interface AuthService {
    UserRegistrationResponse signup(RegisterUserRequest request);

    LoginResponse login(LoginRequest request);

    UserRegistrationResponse confirmUser(String token);

    String recoveryPassword(RecoveryPassRequest request);
}
