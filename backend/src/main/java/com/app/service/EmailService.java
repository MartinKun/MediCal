package com.app.service;

import com.app.controller.dto.EmailDTO;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO);

}