package com.app.service;

import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.RegisterUserResponse;

public interface AuthService {
    RegisterUserResponse register(RegisterUserRequest request);
}
