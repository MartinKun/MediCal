package com.app.service;

import com.app.common.enums.TokenType;
import com.app.controller.dto.enums.GenderEnum;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.request.ResetPassRequest;
import com.app.controller.dto.response.DoctorRegistrationResponse;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.PatientRegistrationResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import com.app.service.implementation.AuthServiceImpl;
import com.app.common.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Mock
    private AuthenticationManager authenticationManager;

    private LoginRequest loginRequest;
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        patientRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("MyPassw@rd123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(GenderEnum.MALE)
                .phone("123456789")
                .role(RoleEnum.PATIENT)
                .address("Mitre 123")
                .build();

        doctorRequest = RegisterUserRequest.builder()
                .firstName("Dr. Smith")
                .lastName("Johnson")
                .email("drsmith@mail.com")
                .password("MyPassw@rd123")
                .birthDate(LocalDate.of(1980, 2, 2))
                .gender(GenderEnum.MALE)
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
        user.setGender(GenderEnum.MALE);
        user.setPhone("123456789");
        user.setEnabled(false);

        loginRequest = LoginRequest.builder()
                .email("johndoe@mail.com")
                .password("1234567")
                .build();

        authentication = Mockito.mock(Authentication.class);
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
        savedPatient.setPassword("MyPassw@rd123");
        savedPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        savedPatient.setGender(GenderEnum.MALE);
        savedPatient.setPhone("123456789");

        Mockito.when(passwordEncoder.encode(patientRequest.getPassword())).thenReturn("MyPassw@rd123");
        Mockito.when(userRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // Act
        PatientRegistrationResponse response = (PatientRegistrationResponse) authServiceImpl.register(patientRequest);

        // Assert
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("Mitre 123", response.getAddress());
        assertEquals("johndoe@mail.com", response.getEmail());
        assertEquals("MyPassw@rd123", response.getPassword());
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
        savedDoctor.setGender(GenderEnum.MALE);
        savedDoctor.setPhone("987654321");

        Mockito.when(passwordEncoder.encode(doctorRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        // Act
        DoctorRegistrationResponse response = (DoctorRegistrationResponse) authServiceImpl.register(doctorRequest);

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


        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
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

    @Test
    @DisplayName("Test 4: User Login Successfully")
    @Order(4)
    public void loginUserTest() {
        // Arrange
        String token = "generatedJwtToken";

        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        )).thenReturn(authentication);

        Mockito.when(jwtUtils.createAccessToken(authentication)).thenReturn(token);

        // Act
        LoginResponse response = authServiceImpl.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(token, response.getToken());

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtUtils).createAccessToken(authentication);
    }

    @Test
    @DisplayName("Test 5: Confirm User Successfully")
    @Order(5)
    public void confirmUserTest() {
        // Arrange
        String token = "confirmToken";
        String email = "johndoe@mail.com";

        DecodedJWT decodedJWT = Mockito.mock(DecodedJWT.class);
        User user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setEmail(email);
        user.setEnabled(false);

        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        UserRegistrationResponse response = authServiceImpl.confirmUser(token);

        // Assert
        assertNotNull(response);
        assertTrue(user.isEnabled());
        Mockito.verify(jwtUtils).validateToken(token, TokenType.CONFIRM);
        Mockito.verify(jwtUtils).extractUsername(decodedJWT);
        Mockito.verify(userRepository).findUserByEmail(email);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test 6: Generate Confirm Token Successfully")
    @Order(6)
    public void generateConfirmTokenTest() {
        // Arrange
        String email = "johndoe@mail.com";
        String token = "generatedConfirmToken";

        Mockito.when(jwtUtils.createConfirmToken(email)).thenReturn(token);

        // Act
        String result = authServiceImpl.generateConfirmToken(email);

        // Assert
        assertNotNull(result);
        assertEquals(token, result);
        Mockito.verify(jwtUtils).createConfirmToken(email);
    }

    @Test
    @DisplayName("Test 7: Generate Password Reset Token Successfully")
    @Order(7)
    public void generatePasswordResetTokenTest() {
        // Arrange
        String email = "johndoe@mail.com";
        String token = "generatedResetToken";

        Mockito.when(jwtUtils.createResetPasswordToken(email)).thenReturn(token);

        // Act
        String result = authServiceImpl.generatePasswordResetToken(email);

        // Assert
        assertNotNull(result);
        assertEquals(token, result);
        Mockito.verify(jwtUtils).createResetPasswordToken(email);
    }

    @Test
    @DisplayName("Test 8: Check if Email Exists")
    @Order(8)
    public void emailExistsTest() {
        // Arrange
        String email = "johndoe@mail.com";

        User user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setEmail(email);
        user.setEnabled(false);

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = authServiceImpl.emailExists(email);

        // Assert
        assertTrue(result);
        Mockito.verify(userRepository).findUserByEmail(email);
    }

    @Test
    @DisplayName("Test 9: Reset Password Successfully")
    @Order(9)
    public void resetPasswordTest() {
        // Arrange
        String token = "resetToken123";
        String newPassword = "newSecurePassword";
        String email = "johndoe@mail.com";

        ResetPassRequest request = ResetPassRequest.builder()
                .token(token)
                .newPassword(newPassword)
                .build();

        DecodedJWT decodedJWT = Mockito.mock(DecodedJWT.class);

        User user = Patient.builder()
                .address("Mitre 123")
                .build();
        user.setEmail(email);
        user.setPassword("oldPasswordHashed");

        // Mocking behavior
        Mockito.when(jwtUtils.validateToken(token, TokenType.RESET)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordHashed");

        // Act
        authServiceImpl.resetPassword(request);

        // Assert
        assertEquals("newPasswordHashed", user.getPassword());
        Mockito.verify(userRepository).findUserByEmail(email);
        Mockito.verify(passwordEncoder).encode(newPassword);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test 10: Email Exists")
    @Order(10)
    public void testEmailExists() {
        // Arrange
        String existingEmail = "existing@mail.com";
        String nonExistingEmail = "nonexisting@mail.com";

        // Mocking behavior
        Mockito.when(userRepository.findUserByEmail(existingEmail)).thenReturn(Optional.of(new Patient()));
        Mockito.when(userRepository.findUserByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertTrue(authServiceImpl.emailExists(existingEmail), "Expected email to exist");
        assertFalse(authServiceImpl.emailExists(nonExistingEmail), "Expected email not to exist");
        Mockito.verify(userRepository).findUserByEmail(existingEmail);
        Mockito.verify(userRepository).findUserByEmail(nonExistingEmail);
    }

}