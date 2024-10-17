package com.app.controller.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPassRequest {

    private String token;
    private String newPassword;
}
