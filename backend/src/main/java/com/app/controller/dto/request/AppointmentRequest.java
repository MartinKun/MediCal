package com.app.controller.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {
    private String patientEmail;
    private LocalDateTime date;
    private String reason;
    private String address;
}
