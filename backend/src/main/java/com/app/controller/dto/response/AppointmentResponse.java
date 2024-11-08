package com.app.controller.dto.response;

import com.app.controller.dto.enums.RoleEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private String reason;
    private String address;
    private String date;
    private ParticipantInfo participant; // Data for the counterpart in the appointment (patient or doctor)

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParticipantInfo {
        private Long id;
        private RoleEnum role;
        private String fullName;
        private String avatar;
    }
}
