package com.app.persistence.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient extends User {
    private String address;
}
