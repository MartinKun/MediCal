package com.app.controller;

import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.RegisterDoctorResponse;
import com.app.controller.dto.response.RegisterPatientResponse;
import com.app.controller.dto.response.RegisterUserResponse;
import com.app.exception.IncompleteFieldsException;
import com.app.service.implementation.AuthServiceImpl;
import com.app.service.implementation.EmailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    @BeforeEach
    public void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        patientRequest = new RegisterUserRequest();
        patientRequest.setFirstName("John");
        patientRequest.setLastName("Doe");
        patientRequest.setEmail("johndoe@mail.com");
        patientRequest.setPassword("password123");
        patientRequest.setBirthDate(
                LocalDate.of(1990, 1, 1)
        );
        patientRequest.setGender("male");
        patientRequest.setPhone("123456");
        patientRequest.setRole(RoleEnum.PATIENT);
        patientRequest.setAddress("123 Street");

        doctorRequest = new RegisterUserRequest();
        doctorRequest.setFirstName("Jane");
        doctorRequest.setLastName("Smith");
        doctorRequest.setEmail("janesmith@mail.com");
        doctorRequest.setPassword("password123");
        doctorRequest.setBirthDate(
                LocalDate.of(1990, 1, 1)
        );
        doctorRequest.setGender("male");
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

        RegisterUserResponse patientResponse = RegisterPatientResponse
                .builder()
                .address("123 Street")
                .build();
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setEmail("johndoe@mail.com");
        patientResponse.setPassword("password123");
        patientResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        patientResponse.setGender("male");
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
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.enabled").value(false));

        verify(authServiceImpl).register(any(RegisterUserRequest.class));
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: successful doctor registration")
    public void testRegisterDoctor() throws Exception {
        RegisterUserResponse doctorResponse = RegisterDoctorResponse.builder()
                .license("LICENSE123")
                .speciality("Cardiology")
                .officeAddress("456 Avenue")
                .build();
        doctorResponse.setFirstName("Jane");
        doctorResponse.setLastName("Smith");
        doctorResponse.setEmail("janesmith@mail.com");
        doctorResponse.setPassword("password123");
        doctorResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        doctorResponse.setGender("female");
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
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.license").value("LICENSE123"))
                .andExpect(jsonPath("$.speciality").value("Cardiology"))
                .andExpect(jsonPath("$.officeAddress").value("456 Avenue"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("DOCTOR"))
                .andExpect(jsonPath("$.enabled").value(false));

        verify(authServiceImpl).register(any(RegisterUserRequest.class));
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: register patient with missing fields")
    public void testRegisterPatientMissingFields() throws Exception {
        patientRequest.setAddress(null);

        when(authServiceImpl.register(any(RegisterUserRequest.class)))
                .thenThrow(new IncompleteFieldsException("Incomplete fields for Patient"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.error").value("Incomplete fields for Patient"));
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: register doctor with missing fields")
    public void testRegisterDoctorMissingFields() throws Exception {
        doctorRequest.setOfficeAddress(null);

        when(authServiceImpl.register(any(RegisterUserRequest.class)))
                .thenThrow(new IncompleteFieldsException("Incomplete fields for Doctor"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.error").value("Incomplete fields for Doctor"));
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: enable user with valid token")
    public void testEnableUser() throws Exception {
        String validToken = "Bearer validJwtToken";

        RegisterUserResponse patientResponse = RegisterPatientResponse
                .builder()
                .address("123 Street")
                .build();
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setEmail("johndoe@mail.com");
        patientResponse.setPassword("password123");
        patientResponse.setBirthDate(LocalDate.of(1990, 1, 1));
        patientResponse.setGender("male");
        patientResponse.setPhone("123456");
        patientResponse.setRole(RoleEnum.PATIENT);
        patientResponse.setEnabled(true);

        when(authServiceImpl.enableUser(any(String.class))).thenReturn(patientResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/auth/enableUser")
                        .header(HttpHeaders.AUTHORIZATION, validToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@mail.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.address").value("123 Street"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.enabled").value(true));

        verify(authServiceImpl).enableUser(any(String.class));
    }

}
