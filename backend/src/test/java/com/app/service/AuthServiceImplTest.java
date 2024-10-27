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
import com.app.exception.UserAlreadyEnabledException;
import com.app.exception.UserDoesNotExistException;
import com.app.exception.UserNotEnabledException;
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
    @DisplayName("Test 1: Register a Patient User Successfully with Valid Data")
    @Order(1)
    public void shouldRegisterPatientSuccessfully_whenValidDataIsProvided() {
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
    @DisplayName("Test 2: Register a Doctor User Successfully with Valid Data")
    @Order(2)
    public void shouldRegisterDoctorSuccessfully_whenValidDataIsProvided() {
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
    @DisplayName("Test 3: Enable a User Successfully with a Valid Token")
    @Order(3)
    public void shouldEnableUserSuccessfully_whenValidTokenIsProvided() {
        // Arrange
        String token = "validToken";
        String email = "johndoe@mail.com";

        user.setEnabled(false);

        User userAfterConfirm = Patient.builder()
                .address("Mitre 123")
                .build();
        userAfterConfirm.setFirstName("John");
        userAfterConfirm.setLastName("Doe");
        userAfterConfirm.setEmail(email);
        userAfterConfirm.setPassword("encodedPassword");
        userAfterConfirm.setBirthDate(LocalDate.of(1990, 1, 1));
        userAfterConfirm.setGender(GenderEnum.MALE);
        userAfterConfirm.setPhone("123456789");
        userAfterConfirm.setEnabled(true);


        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);

        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(user)).thenReturn(userAfterConfirm);

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
    @DisplayName("Login User Successfully with Valid Credentials")
    @Order(4)
    public void shouldLoginUserSuccessfully_whenValidCredentialsIsProvidedTest() {
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
    @DisplayName("Test 5: Confirm User Successfully with Valid Token")
    @Order(5)
    public void shouldConfirmUserSuccessfully_whenValidTokenIsProvidedTest() {
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
    @DisplayName("Test 6: Generate Confirm Token Successfully with Valid Username")
    @Order(6)
    public void shouldGenerateConfirmTokenSuccessfully_whenValidUsernameIsProvidedTest() {
        // Arrange
        String username = "johndoe@mail.com";
        String token = "generatedConfirmToken";

        Mockito.when(jwtUtils.createConfirmToken(username)).thenReturn(token);

        // Act
        String result = authServiceImpl.generateConfirmToken(username);

        // Assert
        assertNotNull(result);
        assertEquals(token, result);
        Mockito.verify(jwtUtils).createConfirmToken(username);
    }

    @Test
    @DisplayName("Test 7: Generate Password Reset Token Successfully with Valid Username")
    @Order(7)
    public void shouldGeneratePasswordResetTokenSuccessfully_whenValidUsernameIsProvidedTest() {
        // Arrange
        String username = "johndoe@mail.com";
        String token = "generatedResetToken";

        Mockito.when(jwtUtils.createResetPasswordToken(username)).thenReturn(token);

        // Act
        String result = authServiceImpl.generatePasswordResetToken(username);

        // Assert
        assertNotNull(result);
        assertEquals(token, result);
        Mockito.verify(jwtUtils).createResetPasswordToken(username);
    }

    @Test
    @DisplayName("Test 8: Check if Email Exists Successfully")
    @Order(8)
    public void shouldCheckIfEmailExists_whenEmailIsProvided() {
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
    @DisplayName("Test 9: Reset Password Successfully When Valid Token and New Password Are Provided")
    @Order(9)
    public void shouldResetPasswordSuccessfully_whenValidTokenAndNewPasswordIsProvided() {
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
    @Order(10)
    @DisplayName("Test 10: Confirm User - User Does Not Exist Exception")
    public void shouldThrowUserDoesNotExistException_whenUserDoesNotExist() {
        // Arrange
        String token = "validToken";
        String email = "nonexistent@mail.com";

        // Mocking behavior
        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDoesNotExistException.class, () -> authServiceImpl.confirmUser(token));

        Mockito.verify(userRepository).findUserByEmail(email);
    }

    @Test
    @Order(11)
    @DisplayName("Test 11: Confirm User - User Already Enabled Exception")
    public void shouldThrowUserAlreadyEnabledException_whenUserIsAlreadyEnabled() {
        // Arrange
        String token = "validToken";
        String email = "enableduser@mail.com";

        User enabledUser = Patient.builder()
                .address("Mitre 123")
                .build();
        enabledUser.setEmail(email);
        enabledUser.setEnabled(true);

        // Mock behavior
        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(enabledUser));

        // Act & Assert
        assertThrows(UserAlreadyEnabledException.class, () -> authServiceImpl.confirmUser(token));

        Mockito.verify(userRepository).findUserByEmail(email);
    }

    @Test
    @Order(12)
    @DisplayName("Test 12: Login - User Not Enabled Exception")
    public void shouldThrowUserNotEnabledException_whenUserIsAlreadyEnabled() {
        // Arrange
        String token = "validToken";
        String email = "enableduser@mail.com";

        User enabledUser = Patient.builder()
                .address("Mitre 123")
                .build();
        enabledUser.setEmail(email);
        enabledUser.setEnabled(true);

        // Mock behavior
        Mockito.when(jwtUtils.validateToken(token, TokenType.CONFIRM)).thenReturn(decodedJWT);
        Mockito.when(jwtUtils.extractUsername(decodedJWT)).thenReturn(email);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(enabledUser));

        // Act & Assert
        assertThrows(UserAlreadyEnabledException.class, () -> authServiceImpl.confirmUser(token));

        Mockito.verify(userRepository).findUserByEmail(email);
    }

    @Test
    @Order(13)
    @DisplayName("Test 13: Login - User Not Enabled Exception")
    public void shouldThrowUserNotEnabledException_whenUserIsNotEnabled() {
        // Arrange
        String email = "notenableduser@mail.com";
        String password = "password123";

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        User notEnabledUser = Patient.builder()
                .address("Mitre 123")
                .build();
        notEnabledUser.setEmail(email);
        notEnabledUser.setEnabled(false);

        // Mock behavior
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(notEnabledUser));

        // Act & Assert
        assertThrows(UserNotEnabledException.class, () -> authServiceImpl.login(request));

        Mockito.verify(userRepository).findUserByEmail(email);
    }

}