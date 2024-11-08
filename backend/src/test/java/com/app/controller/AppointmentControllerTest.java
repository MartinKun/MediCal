package com.app.controller;

import com.app.common.util.DateUtils;
import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.resolvers.CustomUsernameArgumentResolver;
import com.app.service.implementation.AppointmentServiceImpl;
import com.app.service.implementation.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentServiceImpl appointmentService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AppointmentController appointmentController;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "testUser@mail.com"; // The username replaces the @AuthenticationPrincipal parameter from CustomUsernameArgumentResolver.java with a predefined value for the test.

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .setCustomArgumentResolvers(new CustomUsernameArgumentResolver())
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("Test: Successful Appointment Creation")
    void createAppointmentTest() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        Patient patient = new Patient();
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");
        when(userDetailService.loadUserByUsername("matiasclauss@mail.com")).thenReturn(patient);

        AppointmentRequest request = AppointmentRequest.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .patientEmail("matiasclauss@mail.com")
                .build();

        AppointmentResponse response = AppointmentResponse.builder()
                .participant(AppointmentResponse
                        .ParticipantInfo
                        .builder()
                        .fullName(String.format(
                                "%s %s",
                                patient.getFirstName(),
                                patient.getLastName()))
                        .build())
                .date(DateUtils.formatDate(LocalDateTime.of(2024, 12, 4, 14, 30)))
                .address("Calle 13")
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .build();

        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(response);

        this.mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participant.fullName").value("Matías Clauss"))
                .andExpect(jsonPath("$.participant.avatar").doesNotExist())
                .andExpect(jsonPath("$.date").value("2024-12-04T14:30:00"))
                .andExpect(jsonPath("$.address").value("Calle 13"))
                .andExpect(jsonPath("$.reason").value("Evaluación cardiovascular de rutina para control y seguimiento médico."));
    }

    @Test
    @DisplayName("Test: Should return UnauthorizedAppointmentCreationException when a non-doctor tries to create an appointment")
    void shouldReturnUnauthorized_WhenNonDoctorTriesToCreateAppointment() throws Exception {
        // Arrange
        Patient patientUser = new Patient();
        patientUser.setEmail(username);
        when(userDetailService.loadUserByUsername(any(String.class))).thenReturn(patientUser);

        AppointmentRequest request = AppointmentRequest.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .patientEmail("matiasclauss@mail.com")
                .build();

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.errors.error").value("Only doctors can create appointments."));
    }

    @Test
    @DisplayName("Test: Should return Unauthorized when the patient email is not associated with a patient")
    void shouldReturnUnauthorized_WhenPatientEmailIsNotForPatient() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        Doctor nonPatientUser = new Doctor();
        nonPatientUser.setEmail("matiasclauss@mail.com");
        when(userDetailService.loadUserByUsername("matiasclauss@mail.com")).thenReturn(nonPatientUser);

        AppointmentRequest request = AppointmentRequest.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .patientEmail("matiasclauss@mail.com")
                .build();

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.errors.error").value("User with email matiasclauss@mail.com is not a Patient"));
    }

    @Test
    @DisplayName("Test: Should return Appointments when valid User and Month were provided")
    void shouldReturnAppointments_WhenValidUserAndMonthProvided() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        Patient patient = new Patient();
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        AppointmentResponse appointment1 = AppointmentResponse.builder()
                .id(1L)
                .participant(AppointmentResponse.ParticipantInfo.builder()
                        .id(patient.getId())
                        .fullName(String.format("%s %s", patient.getFirstName(), patient.getLastName()))
                        .build())
                .date("2024-12-04T14:30:00")
                .address("Calle 13")
                .reason("Evaluación cardiovascular de rutina.")
                .build();

        AppointmentResponse appointment2 = AppointmentResponse.builder()
                .id(2L)
                .participant(AppointmentResponse.ParticipantInfo.builder()
                        .id(patient.getId())
                        .fullName("Matías Clauss")
                        .build())
                .date("2024-12-05T10:00:00")
                .address("Calle 45")
                .reason("Consulta de seguimiento.")
                .build();

        Set<AppointmentResponse> appointments = Set.of(appointment1, appointment2);
        when(appointmentService.listAppointmentsByUserAndMonth(any(User.class), eq(12), eq(2024)))
                .thenReturn(appointments);

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/appointments")
                        .param("month", "12")
                        .param("year", "2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].participant.fullName").value("Matías Clauss"))
                .andExpect(jsonPath("$[0].date").value("2024-12-04T14:30:00"))
                .andExpect(jsonPath("$[0].address").value("Calle 13"))
                .andExpect(jsonPath("$[0].reason").value("Evaluación cardiovascular de rutina."))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].participant.fullName").value("Matías Clauss"))
                .andExpect(jsonPath("$[1].date").value("2024-12-05T10:00:00"))
                .andExpect(jsonPath("$[1].address").value("Calle 45"))
                .andExpect(jsonPath("$[1].reason").value("Consulta de seguimiento."));
    }

    @Test
    @DisplayName("Test: Should return BadRequest when invalid month is provided")
    void shouldReturnBadRequest_WhenInvalidMonthProvided() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/appointments")
                        .param("month", "13") // Invalid month
                        .param("year", "2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.error").value("Invalid month. Please provide a value between 1 and 12."));
    }

    @Test
    @DisplayName("Test: Should return BadRequest when invalid year is provided")
    void shouldReturnBadRequest_WhenInvalidYearProvided() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/appointments")
                        .param("month", "12")
                        .param("year", "-2024") // Invalid year
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.error").value("Invalid year. Please provide a value between 2000 and " + (LocalDateTime.now().getYear() + 10) + "."));
    }

    @Test
    @DisplayName("Test: Should return BadRequest when invalid user is provided")
    void shouldReturnBadRequest_WhenInvalidUserProvided() throws Exception {
        // Arrange
        when(userDetailService.loadUserByUsername(username)).thenThrow(new UserDoesNotExistException());

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/appointments")
                        .param("month", "12")
                        .param("year", "2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errors.error").value("User does not exist."));
    }
}
