package com.app.service;

import com.app.controller.dto.request.AppointmentRequest;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.AppointmentNotFoundException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        AppointmentResponse response = appointmentService.createAppointment(appointment);

        // Assert
        assertEquals("2024-12-04T14:30:00", response.getDate());
        assertEquals("Matías Clauss", response.getParticipant().getFullName());
        assertEquals("Calle 13", response.getAddress());
        assertEquals("Evaluación cardiovascular de rutina para control y seguimiento médico.", response.getReason());
        assertNull(response.getParticipant().getAvatar());

        // Verify that the repository save method was called
        verify(appointmentRepository).save(Mockito.any(Appointment.class));
    }

    @Test
    @DisplayName("Test: Should return Appointments for a given Patient")
    public void shouldReturnAppointmentsForPatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");

        Doctor doctor = new Doctor();
        doctor.setFirstName("Dr.");
        doctor.setLastName("Smith");

        Appointment appointment = Appointment.builder()
                .id(1L)
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Consulta general")
                .address("Consultorio 1")
                .patient(patient)
                .doctor(doctor)
                .build();

        Set<Appointment> appointments = Set.of(appointment);

        when(appointmentRepository.findAppointmentsByPatientAndMonth(eq(patient.getId()), anyInt(), anyInt()))
                .thenReturn(appointments);

        // Act
        Set<AppointmentResponse> response = appointmentService.listAppointmentsByUserAndMonth(patient, 12, 2024);

        // Assert
        assertEquals(1, response.size());
        AppointmentResponse appointmentResponse = response.iterator().next();
        assertEquals(1L, appointmentResponse.getId());
        assertEquals("Consulta general", appointmentResponse.getReason());
        assertEquals("Consultorio 1", appointmentResponse.getAddress());
        assertEquals("2024-12-04T14:30:00", appointmentResponse.getDate());
        assertEquals("Dr. Smith", appointmentResponse.getParticipant().getFullName());

        // Verify
        verify(appointmentRepository).findAppointmentsByPatientAndMonth(eq(patient.getId()), eq(12), eq(2024));
    }

    @Test
    @DisplayName("Test: Should return Appointments for a given Doctor")
    public void shouldReturnAppointmentsForDoctor() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setFirstName("Dr.");
        doctor.setLastName("House");

        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Johnson");

        Appointment appointment = Appointment.builder()
                .id(2L)
                .date(LocalDateTime.of(2024, 12, 5, 10, 0))
                .reason("Control de rutina")
                .address("Hospital Central")
                .doctor(doctor)
                .patient(patient)
                .build();

        Set<Appointment> appointments = Set.of(appointment);

        when(appointmentRepository.findAppointmentsByDoctorAndMonth(eq(doctor.getId()), anyInt(), anyInt()))
                .thenReturn(appointments);

        // Act
        Set<AppointmentResponse> response = appointmentService.listAppointmentsByUserAndMonth(doctor, 12, 2024);

        // Assert
        assertEquals(1, response.size());
        AppointmentResponse appointmentResponse = response.iterator().next();
        assertEquals(2L, appointmentResponse.getId());
        assertEquals("Control de rutina", appointmentResponse.getReason());
        assertEquals("Hospital Central", appointmentResponse.getAddress());
        assertEquals("2024-12-05T10:00:00", appointmentResponse.getDate());
        assertEquals("Alice Johnson", appointmentResponse.getParticipant().getFullName());

        // Verify
        verify(appointmentRepository).findAppointmentsByDoctorAndMonth(eq(doctor.getId()), eq(12), eq(2024));
    }

    @Test
    @DisplayName("Test: Should return an empty set when no appointments found for Patient")
    public void shouldReturnEmptySet_WhenNoAppointmentsForPatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(3L);

        when(appointmentRepository.findAppointmentsByPatientAndMonth(eq(patient.getId()), anyInt(), anyInt()))
                .thenReturn(Set.of());

        // Act
        Set<AppointmentResponse> response = appointmentService.listAppointmentsByUserAndMonth(patient, 1, 2023);

        // Assert
        assertEquals(0, response.size());

        // Verify
        verify(appointmentRepository).findAppointmentsByPatientAndMonth(eq(patient.getId()), eq(1), eq(2023));
    }

    @Test
    @DisplayName("Test: Should return an empty set when no appointments found for Doctor")
    public void shouldReturnEmptySet_WhenNoAppointmentsForDoctor() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setId(4L);

        when(appointmentRepository.findAppointmentsByDoctorAndMonth(eq(doctor.getId()), anyInt(), anyInt()))
                .thenReturn(Set.of());

        // Act
        Set<AppointmentResponse> response = appointmentService.listAppointmentsByUserAndMonth(doctor, 2, 2022);

        // Assert
        assertEquals(0, response.size());

        // Verify
        verify(appointmentRepository).findAppointmentsByDoctorAndMonth(eq(doctor.getId()), eq(2), eq(2022));
    }

    @Test
    @DisplayName("Test: Should return Appointment by ID")
    public void shouldReturnAppointmentById() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .reason("Consulta de control")
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        Appointment foundAppointment = appointmentService.getAppointmentById(appointmentId);

        // Assert
        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());
        assertEquals("Consulta de control", foundAppointment.getReason());

        // Verify
        verify(appointmentRepository).findById(appointmentId);
    }

    @Test
    @DisplayName("Test: Should throw AppointmentNotFoundException when Appointment not found")
    public void shouldThrowExceptionWhenAppointmentNotFound() {
        // Arrange
        Long nonExistentAppointmentId = 999L;
        when(appointmentRepository.findById(nonExistentAppointmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.getAppointmentById(nonExistentAppointmentId);
        });

        // Verify
        verify(appointmentRepository).findById(nonExistentAppointmentId);
    }

    @Test
    @DisplayName("Test: Should delete Appointment by ID")
    public void shouldDeleteAppointment() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .reason("Consulta para eliminar")
                .build();

        doNothing().when(appointmentRepository).delete(appointment);

        // Act
        appointmentService.deleteAppointment(appointment);

        // Verify
        verify(appointmentRepository, times(1)).delete(appointment);
    }

}
