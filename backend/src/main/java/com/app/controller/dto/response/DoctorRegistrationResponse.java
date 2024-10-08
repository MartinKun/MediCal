package com.app.controller.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRegistrationResponse extends UserRegistrationResponse {
    private String speciality;
    private String license;
    private String officeAddress;
}
