package com.app.service;

import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.DoctorRegistrationResponse;
import com.app.controller.dto.response.PatientRegistrationResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import com.app.service.implementation.AuthServiceImpl;
import com.app.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private RegisterUserRequest patientRequest;
    private RegisterUserRequest doctorRequest;

    private DecodedJWT decodedJWT;
    private User user;

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

        decodedJWT = Mockito.mock(DecodedJWT.class);
        user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@mail.com");
        user.setPassword("encodedPassword");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setGender("male");
        user.setPhone("123456789");
        user.setEnabled(false);
    }

    @Test
    @DisplayName("Test 1: Register a Patient User Successfully")
    @Order(1)
    public void patientRegistrationTest() {
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
        PatientRegistrationResponse response = (PatientRegistrationResponse) authServiceImpl.signup(patientRequest);

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
    public void doctorRegistrationTest() {
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
        DoctorRegistrationResponse response = (DoctorRegistrationResponse) authServiceImpl.signup(doctorRequest);

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

    @Test
    @DisplayName("Test 3: Enable a User Successfully")
    @Order(3)
    public void enableUserTest() {
        // Arrange
        String token = "validToken";
        String email = "johndoe@mail.com";


        Mockito.when(jwtUtils.validateToken(token)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        user.setEnabled(true);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        UserRegistrationResponse response = authServiceImpl.confirmUser(token);

        // Assert
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("johndoe@mail.com", response.getEmail());
        assertTrue(response.isEnabled());

        Mockito.verify(userRepository).save(user);
        assertTrue(user.isEnabled());
    }
}