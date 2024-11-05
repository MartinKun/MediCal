package com.app.controller;

import com.app.common.util.DateUtils;
import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .setCustomArgumentResolvers(new CustomUsernameArgumentResolver())
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
}
