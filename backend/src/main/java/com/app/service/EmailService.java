package com.app.service;

import com.app.controller.dto.EmailDTO;
import com.app.controller.dto.response.UserRegistrationResponse;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO);

    void sendConfirmUserEmail(UserRegistrationResponse userRegistrationResponse);

    void sendRecoveryPassEmail(String email, String newPassword);
}