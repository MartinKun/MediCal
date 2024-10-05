package com.app.repository;

import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test 1:Save Patient User Test")
    @Order(1)
    @Rollback(value = false)
    public void savePatientUserTest(){

        //Action
        User user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@mail.com");
        user.setPassword("1234567");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setGender("male");
        user.setPhone("123456789");
        user.setEnabled(false);

       Patient response = (Patient) userRepository.save(user);

        //Verify
        assertEquals("Mitre 123", response.getAddress());
    }

    @Test
    @DisplayName("Test 2:Save Doctor User Test")
    @Order(2)
    @Rollback(value = false)
    public void saveDoctorUserTest(){

        //Action
        User user = Doctor.builder()
                .speciality("Cardiology")
                .license("MD123456")
                .officeAddress("123 Main St, Springfield")
                .build();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@mail.com");
        user.setPassword("1234567");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setGender("male");
        user.setPhone("123456789");
        user.setEnabled(false);

        Doctor response = (Doctor) userRepository.save(user);

        //Verify
        assertEquals("Cardiology", response.getSpeciality());
        assertEquals("MD123456", response.getLicense());
        assertEquals("123 Main St, Springfield", response.getOfficeAddress());
    }


}
