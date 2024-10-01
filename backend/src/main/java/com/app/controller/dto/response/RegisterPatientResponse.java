package com.app.controller.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterPatientResponse extends RegisterUserResponse{
    private String address;
}
