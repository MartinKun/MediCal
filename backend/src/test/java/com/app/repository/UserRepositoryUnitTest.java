package com.app.repository;

import com.app.controller.dto.enums.GenderEnum;
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
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test 1: Save and Find Patient User by Email")
    @Order(1)
    @Rollback(value = false)
    public void saveAndFindPatientUserByEmailTest() {
        // Arrange
        User user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@mail.com");
        user.setPassword("MyPassw@rd123");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setGender(GenderEnum.MALE);
        user.setPhone("123456789");
        user.setEnabled(false);

        // Act
        userRepository.save(user);

        // Verify
        Optional<User> retrievedUser = userRepository.findUserByEmail("johndoe@mail.com");
        assertTrue(retrievedUser.isPresent());
        assertEquals("John", retrievedUser.get().getFirstName());
        assertEquals("Doe", retrievedUser.get().getLastName());
    }

    @Test
    @DisplayName("Test 2: Save and Find Doctor User by Email")
    @Order(2)
    @Rollback(value = false)
    public void saveAndFindDoctorUserByEmailTest() {
        // Arrange
        User user = Doctor.builder()
                .speciality("Cardiology")
                .license("MD123456")
                .officeAddress("123 Main St, Springfield")
                .build();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("doctordoe@mail.com");
        user.setPassword("MyPassw@rd123");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setGender(GenderEnum.MALE);
        user.setPhone("123456789");
        user.setEnabled(false);

        // Act
        userRepository.save(user);

        // Verify
        Optional<User> retrievedUser = userRepository.findUserByEmail("doctordoe@mail.com");
        assertTrue(retrievedUser.isPresent());
        assertEquals("Cardiology", ((Doctor) retrievedUser.get()).getSpeciality());
        assertEquals("MD123456", ((Doctor) retrievedUser.get()).getLicense());
        assertEquals("123 Main St, Springfield", ((Doctor) retrievedUser.get()).getOfficeAddress());
    }

    @Test
    @DisplayName("Test 3: Find Non-existent User by Email")
    @Order(3)
    public void findNonExistentUserByEmailTest() {
        // Act
        Optional<User> retrievedUser = userRepository.findUserByEmail("nonexistent@mail.com");

        // Verify
        assertFalse(retrievedUser.isPresent(), "Expected no user to be found with the given email");
    }
}
