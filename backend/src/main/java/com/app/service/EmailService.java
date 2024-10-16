package com.app.service;

import com.app.controller.dto.EmailDTO;
import com.app.controller.dto.response.UserRegistrationResponse;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO);

    void sendConfirmUserEmail(String name, String email, String token);

    void sendResetPassEmail(String email, String token);
}