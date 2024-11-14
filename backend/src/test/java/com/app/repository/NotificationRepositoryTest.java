package com.app.repository;

import com.app.common.enums.NotificationType;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.NotificationRepository;
import com.app.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private Patient savedPatient;

    @BeforeEach
    public void setUp() {
        Patient patient = Patient.builder()
                .address("Calle Falsa 123")
                .build();
        patient.setId(1L);
        patient.setFirstName("Carlos");
        patient.setLastName("Ramirez");
        patient.setEmail("carlos.ramirez@example.com");

        savedPatient = userRepository.save(patient);

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Notification notification = Notification.builder()
                    .date(LocalDateTime.now())
                    .type(NotificationType.INFO)
                    .content("Notificación " + i)
                    .isRead(false)
                    .user(savedPatient)
                    .build();
            notificationRepository.save(notification);
        });
    }

    @Test
    @DisplayName("Test: Find all notifications by userId with pagination")
    @Rollback(value = false)
    public void findAllNotificationsByUserId() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 3);

        // Act
        Page<Notification> notificationsPage = notificationRepository.findAllByUserId(savedPatient.getId(), pageable);

        // Assert
        assertNotNull(notificationsPage);
        assertEquals(3, notificationsPage.getContent().size());
        assertEquals(5, notificationsPage.getTotalElements());
        assertEquals(2, notificationsPage.getTotalPages());
        assertFalse(notificationsPage.isEmpty());

        notificationsPage.getContent().forEach(notification ->
                assertEquals(savedPatient.getId(), notification.getUser().getId())
        );
    }

    @Test
    @DisplayName("Test: Save a notification")
    @Rollback(value = false)
    public void saveNotification() {
        // Arrange
        Notification notification = Notification.builder()
                .date(LocalDateTime.now())
                .type(NotificationType.INFO)
                .content("New appointment scheduled")
                .isRead(false)
                .user(savedPatient)
                .build();

        // Act
        Notification savedNotification = notificationRepository.save(notification);

        // Assert
        assertNotNull(savedNotification.getId());
        assertEquals("New appointment scheduled", savedNotification.getContent());
        assertEquals(NotificationType.INFO, savedNotification.getType());
        assertFalse(savedNotification.isRead());
        assertEquals(savedPatient.getId(), savedNotification.getUser().getId());
    }

    @Test
    @DisplayName("Test: Delete a notification by ID")
    @Rollback(value = false)
    public void deleteNotificationById() {
        // Arrange
        Notification notificationToDelete = Notification.builder()
                .date(LocalDateTime.now())
                .type(NotificationType.CALENDAR)
                .content("This is a test notification to be deleted")
                .isRead(false)
                .user(savedPatient)
                .build();

        Notification savedNotification = notificationRepository.save(notificationToDelete);
        Long notificationId = savedNotification.getId();
        assertNotNull(notificationId);

        // Act
        Optional<Notification> existingNotification = notificationRepository.findById(notificationId);
        assertTrue(existingNotification.isPresent());

        notificationRepository.delete(savedNotification);

        // Assert
        Optional<Notification> deletedNotification = notificationRepository.findById(notificationId);
        assertTrue(deletedNotification.isEmpty());
    }
}
