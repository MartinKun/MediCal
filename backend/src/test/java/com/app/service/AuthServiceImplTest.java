package com.app.service;

import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.RegisterDoctorResponse;
import com.app.controller.dto.response.RegisterPatientResponse;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.repository.UserRepository;
import com.app.service.implementation.AuthServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private RegisterUserRequest patientRequest;
    private RegisterUserRequest doctorRequest;

    @BeforeEach
    public void setup() {
        patientRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("1234567")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("male")
                .phone("123456789")
                .role(RoleEnum.PATIENT)
                .address("Mitre 123")
                .build();

        doctorRequest = RegisterUserRequest.builder()
                .firstName("Dr. Smith")
                .lastName("Johnson")
                .email("drsmith@mail.com")
                .password("password")
                .birthDate(LocalDate.of(1980, 2, 2))
                .gender("male")
                .phone("987654321")
                .role(RoleEnum.DOCTOR)
                .speciality("Cardiology")
                .license("ABC123")
                .officeAddress("Main St 456")
                .build();
    }

    @Test
    @DisplayName("Test 1: Register a Patient User Successfully")
    @Order(1)
    public void registerPatientTest() {
        // Arrange
        Patient savedPatient = Patient.builder()
                .address("Mitre 123")
                .build();
        savedPatient.setFirstName("John");
        savedPatient.setLastName("Doe");
        savedPatient.setEmail("johndoe@mail.com");
        savedPatient.setPassword("encodedPassword");
        savedPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        savedPatient.setGender("male");
        savedPatient.setPhone("123456789");

        Mockito.when(passwordEncoder.encode(patientRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // Act
        RegisterPatientResponse response = (RegisterPatientResponse) authServiceImpl.register(patientRequest);

        // Assert
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("Mitre 123", response.getAddress());
        assertEquals("johndoe@mail.com", response.getEmail());
        assertEquals("encodedPassword", response.getPassword());
        assertEquals(false, response.isEnabled());
        assertEquals(RoleEnum.PATIENT, response.getRole());
    }

    @Test
    @DisplayName("Test 2: Register a Doctor User Successfully")
    @Order(2)
    public void registerDoctorTest() {
        // Arrange
        Doctor savedDoctor = Doctor.builder()
                .speciality("Cardiology")
                .license("ABC123")
                .officeAddress("Main St 456")
                .build();
        savedDoctor.setFirstName("Dr. Smith");
        savedDoctor.setLastName("Johnson");
        savedDoctor.setEmail("drsmith@mail.com");
        savedDoctor.setPassword("encodedPassword");
        savedDoctor.setBirthDate(LocalDate.of(1980, 2, 2));
        savedDoctor.setGender("male");
        savedDoctor.setPhone("987654321");

        Mockito.when(passwordEncoder.encode(doctorRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        // Act
        RegisterDoctorResponse response = (RegisterDoctorResponse) authServiceImpl.register(doctorRequest);

        // Assert
        assertEquals("Dr. Smith", response.getFirstName());
        assertEquals("Johnson", response.getLastName());
        assertEquals("Cardiology", response.getSpeciality());
        assertEquals("ABC123", response.getLicense());
        assertEquals("Main St 456", response.getOfficeAddress());
        assertEquals("drsmith@mail.com", response.getEmail());
        assertEquals("encodedPassword", response.getPassword());
        assertEquals(false, response.isEnabled());
        assertEquals(RoleEnum.DOCTOR, response.getRole());
    }
}