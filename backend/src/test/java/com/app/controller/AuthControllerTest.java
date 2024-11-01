package com.app.controller;

import com.app.controller.dto.enums.GenderEnum;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.*;
import com.app.controller.dto.response.DoctorRegistrationResponse;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.PatientRegistrationResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.exception.InvalidTokenException;
import com.app.exception.UserAlreadyEnabledException;
import com.app.exception.UserNotEnabledException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import com.app.validation.RegisterUserRequestValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;

@WebMvcTest(AuthController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authServiceImpl;

    @MockBean
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private RegisterUserRequest patientRequest;
    private RegisterUserRequest doctorRequest;

    @MockBean
    private RegisterUserRequestValidator registerUserRequestValidator;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        patientRequest = new RegisterUserRequest();
        patientRequest.setFirstName("John");
        patientRequest.setLastName("Doe");
        patientRequest.setEmail("johndoe@mail.com");
        patientRequest.setPassword("MyPassw@rd123");
        patientRequest.setBirthDate(
                LocalDate.of(1990, 1, 1)
        );
        patientRequest.setGender(GenderEnum.MALE);
        patientRequest.setPhone("123456");
        patientRequest.setRole(RoleEnum.PATIENT);
        patientRequest.setAddress("123 Street");

        doctorRequest = new RegisterUserRequest();
        doctorRequest.setFirstName("Jane");
        doctorRequest.setLastName("Smith");
        doctorRequest.setEmail("janesmith@mail.com");
        doctorRequest.setPassword("MyPassw@rd123");
        doctorRequest.setBirthDate(
                LocalDate.of(1990, 1, 1)
        );
        doctorRequest.setGender(GenderEnum.MALE);
        doctorRequest.setPhone("123456");
        doctorRequest.setRole(RoleEnum.DOCTOR);
        doctorRequest.setLicense("LICENSE123");
        doctorRequest.setOfficeAddress("456 Avenue");
        doctorRequest.setSpeciality("Cardiology");
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: successful patient registration")
    public void testRegisterPatient() throws Exception {

        UserRegistrationResponse patientResponse = PatientRegistrationResponse
                .builder()
                .address("123 Street")
                .build();
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setEmail("johndoe@mail.com");
        patientResponse.setPassword("MyPassw@rd123");
        patientResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        patientResponse.setGender(GenderEnum.MALE);
        patientResponse.setPhone("123456");
        patientResponse.setRole(RoleEnum.PATIENT);

        when(authServiceImpl.register(any(RegisterUserRequest.class))).thenReturn(patientResponse);

        this.mockMvc
                .perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patientRequest))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@mail.com"))
                .andExpect(jsonPath("$.password").value("MyPassw@rd123"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.enabled").value(false));

        verify(authServiceImpl).register(any(RegisterUserRequest.class));
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: successful doctor registration")
    public void testRegisterDoctor() throws Exception {
        UserRegistrationResponse doctorResponse = DoctorRegistrationResponse.builder()
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();
        doctorResponse.setFirstName("Jane");
        doctorResponse.setLastName("Smith");
        doctorResponse.setEmail("janesmith@mail.com");
        doctorResponse.setPassword("MyPassw@rd123");
        doctorResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        doctorResponse.setGender(GenderEnum.FEMALE);
        doctorResponse.setPhone("123456");
        doctorResponse.setRole(RoleEnum.DOCTOR);

        when(authServiceImpl.register(any(RegisterUserRequest.class))).thenReturn(doctorResponse);

        this.mockMvc
                .perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(doctorRequest))
                                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("janesmith@mail.com"))
                .andExpect(jsonPath("$.password").value("MyPassw@rd123"))
                .andExpect(jsonPath("$.license").value("LICENSE123"))
                .andExpect(jsonPath("$.speciality").value("Cardiology"))
                .andExpect(jsonPath("$.officeAddress").value("456 Avenue"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("FEMALE"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("DOCTOR"))
                .andExpect(jsonPath("$.enabled").value(false));

        verify(authServiceImpl).register(any(RegisterUserRequest.class));
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: enable user with valid token")
    public void testEnableUser() throws Exception {

        ConfirmUserRequest confirmUserRequest = ConfirmUserRequest.builder()
                .token("validJwtToken")
                .build();

        UserRegistrationResponse patientResponse = PatientRegistrationResponse
                .builder()
                .address("123 Street")
                .build();
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setEmail("johndoe@mail.com");
        patientResponse.setPassword("MyPassw@rd123");
        patientResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        patientResponse.setGender(GenderEnum.MALE);
        patientResponse.setPhone("123456");
        patientResponse.setRole(RoleEnum.PATIENT);
        patientResponse.setEnabled(true);

        when(authServiceImpl.confirmUser(any(String.class))).thenReturn(patientResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/auth/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(confirmUserRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@mail.com"))
                .andExpect(jsonPath("$.password").value("MyPassw@rd123"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.enabled").value(true));

        verify(authServiceImpl).confirmUser(any(String.class));
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Login User Successfully")
    public void testLoginUser() throws Exception {

        LoginRequest request = LoginRequest.builder()
                .email("janesmith@mail.com")
                .password("MyPassw@rd123")
                .build();

        LoginResponse response = LoginResponse.builder()
                .token("validToken")
                .build();

        when(authServiceImpl.login(any(LoginRequest.class))).thenReturn(response);

        this.mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("validToken"));

        verify(authServiceImpl).login(any(LoginRequest.class));
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Forgot Password Successfully")
    public void testForgotPassword() throws Exception {
        ForgotPassRequest request = ForgotPassRequest.builder()
                .email("johndoe@mail.com")
                .build();

        String token = "resetToken123";

        when(authServiceImpl.emailExists(anyString())).thenReturn(true);
        when(authServiceImpl.generatePasswordResetToken(anyString())).thenReturn(token);
        doNothing().when(emailServiceImpl).sendResetPassEmail(anyString(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("If the email exists, a password reset email has been sent."));

        verify(authServiceImpl).emailExists(anyString());
        verify(authServiceImpl).generatePasswordResetToken(anyString());
        verify(emailServiceImpl).sendResetPassEmail(anyString(), anyString());
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Reset Password Successfully")
    public void testResetPassword() throws Exception {
        ResetPassRequest request = ResetPassRequest.builder()
                .token("resetToken123")
                .newPassword("newSecureP@ssword123")
                .build();

        doNothing().when(authServiceImpl).resetPassword(any(ResetPassRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));

        verify(authServiceImpl).resetPassword(any(ResetPassRequest.class));
    }

    /* Validations */

    @Test
    @DisplayName("Test 7: Validate Password Pattern Error On User Registration")
    @Order(7)
    public void invalidPasswordPatternTestOnUserRegistration() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password12345")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.PATIENT)
                .address("123 Main St")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character, and must be at least 8 characters long"));

    }

    @Test
    @DisplayName("Test 8: Validate Password Pattern Error On PasswordReset")
    @Order(8)
    public void invalidPasswordPatternTestOnPasswordReset() throws Exception {
        // Arrange
        ResetPassRequest invalidRequest = ResetPassRequest.builder()
                .token("token123")
                .newPassword("mynewpass")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.newPassword")
                        .value("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character, and must be at least 8 characters long"));

    }

    @Test
    @DisplayName("Test 9: Validate Blank Password Error On User Registration")
    @Order(9)
    public void blankPasswordOnUserRegistrationTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.PATIENT)
                .address("123 Main St")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password is required"));
    }

    @Test
    @DisplayName("Test 10: Validate Blank Password Error On Reset Password")
    @Order(10)
    public void blankPasswordOnResetPasswordTest() throws Exception {
        // Arrange
        ResetPassRequest invalidRequest = ResetPassRequest.builder()
                .token("token123")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.newPassword")
                        .value("Password is required"));
    }

    @Test
    @DisplayName("Test 11: Validate Age for Doctor Registration")
    @Order(11)
    public void underageDoctorTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .birthDate(LocalDate.now().minusYears(17))
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.DOCTOR)
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.birthDate")
                        .value("User must be at least 18 years old."));
    }

    @Test
    @DisplayName("Test 12: Validate Blank Birth Date Error")
    @Order(12)
    public void blankBirthDateTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.DOCTOR)
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.birthDate")
                        .value("Birth date is required"));
    }

    @Test
    @DisplayName("Test 13: Invalid Gender for Doctor Registration")
    @Order(13)
    public void invalidGenderTest() throws Exception {
        // Arrange
        String invalidRequestJson = "{"
                + "\"firstName\":\"John\","
                + "\"lastName\":\"Doe\","
                + "\"email\":\"john.doe@example.com\","
                + "\"password\":\"Myp@ssword123\","
                + "\"gender\":\"RANDOM\"," // Valor inválido
                + "\"phone\":\"1234567890\","
                + "\"role\":\"DOCTOR\","
                + "\"license\":\"LICENSE123\","
                + "\"speciality\":\"Cardiology\","
                + "\"officeAddress\":\"456 Avenue\""
                + "}";


        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.errors.error")
                        .value("JSON parse error: Cannot deserialize value of type `com.app.controller.dto.enums.GenderEnum` from String \"RANDOM\": not one of the values accepted for Enum class: [OTHER, FEMALE, MALE]"));
    }

    @Test
    @DisplayName("Test 14: Validate Blank Gender Error")
    @Order(14)
    public void blankGenderTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .role(RoleEnum.DOCTOR)
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.gender")
                        .value("Gender is required"));
    }

    @Test
    @DisplayName("Test 15: Validate Blank Phone Error")
    @Order(15)
    public void blankPhoneTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .gender(GenderEnum.MALE)
                .birthDate(LocalDate.of(1990, 1, 1))
                .role(RoleEnum.DOCTOR)
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.phone")
                        .value("Phone is required"));
    }

    @Test
    @DisplayName("Test 16: Validate Blank Role Error")
    @Order(16)
    public void blankRoleTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .phone("1234567890")
                .gender(GenderEnum.MALE)
                .birthDate(LocalDate.of(1990, 1, 1))
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.role")
                        .value("Role is required"));
    }

    @Test
    @DisplayName("Test 17: Validate Patient Fields Error")
    @Order(17)
    public void invalidPatientFieldsTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .birthDate(LocalDate.now().minusYears(17))
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.PATIENT)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.address")
                        .value("Address is required for patients"));
    }

    @Test
    @DisplayName("Test 21: Email Already Exists Exception")
    @Order(21)
    public void testEmailAlreadyExistsException() throws Exception {

        RegisterUserRequest request = RegisterUserRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("johndoe@mail.com")
                .password("Secure@123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(GenderEnum.FEMALE)
                .phone("1234567890")
                .role(RoleEnum.PATIENT)
                .address("123 Main St")
                .build();

        when(authServiceImpl.emailExists(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errors.error")
                        .value("The email johndoe@mail.com is already registered."));

        verify(authServiceImpl).emailExists(anyString());
    }

    @Test
    @DisplayName("Test 22: Validate Doctor Fields Error")
    @Order(22)
    public void invalidDoctorFieldsTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("Myp@ssword123")
                .birthDate(LocalDate.now().minusYears(17))
                .gender(GenderEnum.MALE)
                .phone("1234567890")
                .role(RoleEnum.DOCTOR)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.license")
                        .value("License is required for doctors"))
                .andExpect(jsonPath("$.errors.speciality")
                        .value("Speciality is required for doctors"))
                .andExpect(jsonPath("$.errors.officeAddress")
                        .value("Office address is required for doctors"));
    }

    @Test
    @DisplayName("Test 23: Validate Multiple Blank Fields Error")
    @Order(23)
    public void validateMultipleBlankFieldsTest() throws Exception {
        // Arrange
        RegisterUserRequest invalidRequest = RegisterUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .role(RoleEnum.DOCTOR)
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.gender")
                        .value("Gender is required"))
                .andExpect(jsonPath("$.errors.birthDate")
                        .value("Birth date is required"))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password is required"));
    }

    @Test
    @DisplayName("Test 24: Validate Blank Token Error On Reset Password")
    @Order(24)
    public void blankTokenOnResetPasswordTest() throws Exception {
        // Arrange
        ResetPassRequest invalidRequest = ResetPassRequest.builder()
                .newPassword("Mynewp@ss567")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.token")
                        .value("Token is required"));
    }

    /* Exceptions */

    @Test
    @DisplayName("Test 25: User Already Enabled Exception")
    @Order(25)
    public void testUserAlreadyEnabledException() throws Exception {
        // Arrange
        String token = "validToken";
        ConfirmUserRequest request = new ConfirmUserRequest(token);

        when(authServiceImpl.confirmUser(token)).thenThrow(new UserAlreadyEnabledException());

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errors.error")
                        .value("User has already been enabled."));

        verify(authServiceImpl).confirmUser(token);
    }

    @Test
    @DisplayName("Test 26: User Not Enabled Exception On Login")
    @Order(26)
    public void testUserNotEnabledExceptionOnLogin() throws Exception {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .email("johndoe@mail.com")
                .password("Myp@ssword123")
                .build();

        when(authServiceImpl.login(any(LoginRequest.class))).thenThrow(new UserNotEnabledException());

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.errors.error")
                        .value("User is not enabled."));
    }

    @Test
    @DisplayName("Test 27: Authentication Exception")
    @Order(27)
    public void testAuthenticationException() throws Exception {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .email("johndoe@mail.com")
                .password("IncorrectPassword123")
                .build();

        when(authServiceImpl.login(any(LoginRequest.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {
                });

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.errors.error")
                        .value("Bad credentials"));
    }

    @Test
    @DisplayName("Test 28: Invalid Token Exception")
    @Order(28)
    public void testInvalidTokenException() throws Exception {
        // Arrange
        ConfirmUserRequest request = ConfirmUserRequest.builder()
                .token("invalidToken123")
                .build();

        when(authServiceImpl.confirmUser(anyString())).thenThrow(new InvalidTokenException());

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.errors.error")
                        .value("Token invalid, not Authorized"));
    }

    @Test
    @DisplayName("Test 29: User Not Enabled Exception On Reset Password")
    @Order(29)
    public void testUserNotEnabledExceptionOnResetPassword() throws Exception {
        // Arrange
        ResetPassRequest request = ResetPassRequest.builder()
                .token("token123")
                .newPassword("MyNewP@ss567")
                .build();

        doNothing().when(authServiceImpl).resetPassword(any(ResetPassRequest.class));

        // Act & Assert
        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

}
