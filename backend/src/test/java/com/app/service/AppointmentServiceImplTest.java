package com.app.service;

import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.UnauthorizedAppointmentCreationException;
import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.AppointmentRepository;
import com.app.persistence.repository.UserRepository;
import com.app.service.implementation.AppointmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentRequest appointmentRequest;

    private AppointmentResponse appointmentResponse;

    @Test
    @DisplayName("Test: Create an Appointment Successfully with Valid Data")
    public void shouldCreateAppointmentSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        Doctor doctor = Doctor.builder()
                .officeAddress("Avenue 123")
                .license("ABC12345")
                .speciality("Cardiología")
                .build();

        Appointment appointment = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .doctor(doctor)
                .patient(patient)
                .build();

        AppointmentRequest appointmentRequest = AppointmentRequest.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .patientEmail("matiasclauss@mail.com")
                .build();

        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        AppointmentResponse response = appointmentService.createAppointment(appointment);

        // Assert
        assertEquals("2024-12-04T14:30:00", response.getDate());
        assertEquals("Matías Clauss", response.getParticipant().getFullName());
        assertEquals("Calle 13", response.getAddress());
        assertEquals("Evaluación cardiovascular de rutina para control y seguimiento médico.", response.getReason());
        assertNull(response.getParticipant().getAvatar());

        // Verify that the repository save method was called
        Mockito.verify(appointmentRepository).save(Mockito.any(Appointment.class));
    }

}
