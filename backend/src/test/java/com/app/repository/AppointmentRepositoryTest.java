package com.app.repository;

import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.repository.AppointmentRepository;
import com.app.persistence.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    private Patient savedPatient;
    private Doctor savedDoctor;

    @BeforeEach
    public void setUp() {
        // Arrange common test data
        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        Doctor doctor = Doctor.builder()
                .officeAddress("Avenue 123")
                .license("ABC12345")
                .speciality("Cardiología")
                .build();

        savedPatient = userRepository.save(patient);
        savedDoctor = userRepository.save(doctor);
    }

    @Test
    @DisplayName("Test: Save and verify medical appointment")
    @Rollback(value = false)
    public void saveAndVerifyMedicalAppointment() {
        // Arrange
        Appointment appointment = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .doctor(savedDoctor)
                .patient(savedPatient)
                .build();

        // Act
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Verify
        assertNotNull(savedAppointment.getId());
        assertEquals("Calle 13", savedAppointment.getAddress());
        assertEquals("Evaluación cardiovascular de rutina para control y seguimiento médico.", savedAppointment.getReason());
        assertEquals(LocalDateTime.of(2024, 12, 4, 14, 30), savedAppointment.getDate());

        assertEquals(savedPatient.getAddress(), savedAppointment.getPatient().getAddress());
        assertEquals("Avenue 123", savedAppointment.getDoctor().getOfficeAddress());
        assertEquals("ABC12345", savedAppointment.getDoctor().getLicense());
        assertEquals("Cardiología", savedAppointment.getDoctor().getSpeciality());
    }

    @Test
    @DisplayName("Test: Find appointments by doctor and month")
    @Rollback(value = false)
    public void findAppointmentsByDoctorAndMonth() {
        // Arrange
        Appointment appointment1 = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .doctor(savedDoctor)
                .patient(savedPatient)
                .build();

        Appointment appointment2 = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 15, 10, 30))
                .reason("Chequeo de rutina.")
                .address("Calle 14")
                .doctor(savedDoctor)
                .patient(savedPatient)
                .build();

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        // Act
        Set<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorAndMonth(savedDoctor.getId(), 12, 2024);

        // Assert
        assertNotNull(appointments);
        assertEquals(2, appointments.size()); // Ensure two appointments are found for the doctor in December 2024
    }

    @Test
    @DisplayName("Test: Find appointments by patient and month")
    @Rollback(value = false)
    public void findAppointmentsByPatientAndMonth() {
        // Arrange
        Appointment appointment1 = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 4, 14, 30))
                .reason("Evaluación cardiovascular de rutina para control y seguimiento médico.")
                .address("Calle 13")
                .doctor(savedDoctor)
                .patient(savedPatient)
                .build();

        Appointment appointment2 = Appointment.builder()
                .date(LocalDateTime.of(2024, 12, 15, 10, 30))
                .reason("Chequeo de rutina.")
                .address("Calle 14")
                .doctor(savedDoctor)
                .patient(savedPatient)
                .build();

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        // Act
        Set<Appointment> appointments = appointmentRepository.findAppointmentsByPatientAndMonth(savedPatient.getId(), 12, 2024);

        // Assert
        assertNotNull(appointments);
        assertEquals(2, appointments.size()); // Ensure two appointments are found for the patient in December 2024
    }

}
