package com.app.controller.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private int status;
    private Map<String, String> errors;
}
