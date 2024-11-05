package com.app.controller.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private ParticipantInfo participant; // Data for the counterpart in the appointment (patient or doctor)
    private String date;
    private String reason;
    private String address;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParticipantInfo {
        private String fullName;
        private String avatar;
    }
}
