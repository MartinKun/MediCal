package com.app.controller.dto.response;

import com.app.persistence.entity.Patient;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private String userName;
    private String userAvatar;
    private LocalDateTime date;
    private String reason;
    private String address;
}
