package com.app.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPassRequest {

    @NotBlank(message = "Token is required")
    private String token;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character, and must be at least 8 characters long"
    )
    private String newPassword;
}
