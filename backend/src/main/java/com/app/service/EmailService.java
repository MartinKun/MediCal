package com.app.service;

import com.app.controller.dto.EmailDTO;
import com.app.controller.dto.response.RegisterUserResponse;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO);

    void sendConfirmUserEmail(RegisterUserResponse registerUserResponse);

}