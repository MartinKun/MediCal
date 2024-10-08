package com.app.service.implementation;

import com.app.controller.dto.EmailDTO;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.service.EmailService;
import com.app.util.EmailTemplates;
import com.app.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${email.username}")
    private String FROM_EMAIL;

    @Value("${email.password}")
    private String FROM_PASSWORD;

    @Value("${email.host}")
    private String HOST;

    private final String FROM_NAME = "Martín";

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void sendEmail(EmailDTO emailDTO) {

        final String RECIPIENT = emailDTO.getRecipient();
        final String BODY = emailDTO.getBody();
        final String SUBJECT = emailDTO.getSubject();

        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));

            msg.setReplyTo(InternetAddress.parse(FROM_EMAIL, false));

            msg.setSubject(SUBJECT, "UTF-8");

            msg.setContent(BODY, "text/html; charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT, false));
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendConfirmUserEmail(UserRegistrationResponse userRegistrationResponse) {
        String token = jwtUtils.createConfirmToken(userRegistrationResponse.getEmail());

        EmailDTO emailDTO = EmailDTO.builder()
                .recipient(userRegistrationResponse.getEmail())
                .subject("Confirmación de registro en nuestro sitio web")
                .body(EmailTemplates.getConfirmationEmailTemplate(token, userRegistrationResponse.getFirstName()))
                .build();
        this.sendEmail(emailDTO);
    }
}
