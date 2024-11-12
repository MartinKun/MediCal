package com.app.controller.dto.response;

import com.app.controller.dto.enums.GenderEnum;
import com.app.controller.dto.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserProfileResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private GenderEnum gender;
    private String phone;
    private RoleEnum role;
    private String image;
}
