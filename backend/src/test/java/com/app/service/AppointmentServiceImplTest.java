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

        appointmentRequest = AppointmentRequest.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .patientEmail("matiasclauss@mail.com")
                .build();

        Mockito.when(userRepository.findUserByEmail("matiasclauss@mail.com")).thenReturn(Optional.of(patient));
        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        AppointmentResponse response = appointmentService.createAppointment(appointmentRequest, doctor);

        // Assert
        assertEquals(LocalDateTime.of(2024, 12, 4, 14, 30), response.getDate());
        assertEquals("Matías Clauss", response.getUserName());
        assertEquals("Calle 13", response.getAddress());
        assertEquals("Evaluación cardiovascular de rutina para control y seguimiento médico.", response.getReason());
        assertEquals(null, response.getUserAvatar());

        Mockito.verify(appointmentRepository).save(Mockito.any(Appointment.class));
    }

    @Test
    @DisplayName("Test: Throw UnauthorizedAppointmentCreationException when User is not a Doctor")
    public void shouldThrowUnauthorizedAppointmentCreationException_whenUserIsNotDoctor() {
        // Arrange
        User nonDoctorUser = Patient.builder()
                .address("Mitre 123")
                .build();
        nonDoctorUser.setEmail("patient@mail.com");
        AppointmentRequest request = AppointmentRequest.builder()
                .patientEmail("patient@mail.com")
                .build();

        // Act & Assert
        assertThrows(UnauthorizedAppointmentCreationException.class,
                () -> appointmentService.createAppointment(request, nonDoctorUser));
    }

    @Test
    @DisplayName("Test: Throw UserDoesNotExistException when Patient does not exist in repository")
    public void shouldThrowUserDoesNotExistException_whenPatientDoesNotExist() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .officeAddress("Avenue 123")
                .license("ABC12345")
                .speciality("Cardiología")
                .build();
        doctor.setEmail("doctor@mail.com");
        AppointmentRequest request = AppointmentRequest.builder()
                .patientEmail("nonexistentpatient@mail.com")
                .build();

        Mockito.when(userRepository.findUserByEmail("nonexistentpatient@mail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserDoesNotExistException exception = assertThrows(UserDoesNotExistException.class,
                () -> appointmentService.createAppointment(request, doctor));

        assertEquals("Patient with email nonexistentpatient@mail.com does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw UnauthorizedAppointmentCreationException when Found User is not a Patient")
    public void shouldThrowUnauthorizedAppointmentCreationException_whenFoundUserIsNotPatient() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .officeAddress("Avenue 123")
                .license("ABC12345")
                .speciality("Cardiología")
                .build();
        doctor.setEmail("doctor@mail.com");

        User nonPatientUser = Doctor.builder()
                .officeAddress("Street 321")
                .license("UTC12345")
                .speciality("Traumatología")
                .build();
        nonPatientUser.setEmail("user@mail.com");

        AppointmentRequest request = AppointmentRequest.builder()
                .patientEmail("user@mail.com")
                .build();

        Mockito.when(userRepository.findUserByEmail("user@mail.com"))
                .thenReturn(Optional.of(nonPatientUser));

        // Act & Assert
        UnauthorizedAppointmentCreationException exception = assertThrows(UnauthorizedAppointmentCreationException.class,
                () -> appointmentService.createAppointment(request, doctor));

        assertEquals("User with email user@mail.com is not a Patient", exception.getMessage());
    }

}
