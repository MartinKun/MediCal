package com.app.persistence.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor extends User {

    private String speciality;
    private String license;
    private String officeAddress;
}
