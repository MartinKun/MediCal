package com.app.service;

import com.app.common.enums.NotificationType;
import com.app.controller.dto.request.NotificationRequest;
import com.app.controller.dto.response.NotificationResponse;
import com.app.exception.NotificationNotFoundException;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.Patient;
import com.app.persistence.repository.NotificationRepository;
import com.app.service.implementation.NotificationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("Test: List Notifications Successfully by User ID")
    public void shouldListNotificationsSuccessfully_whenUserIdIsProvided() {
        // Arrange
        Long userId = 1L;

        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setId(userId);
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        Notification notification = Notification.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .content("Test Notification")
                .isRead(false)
                .user(patient)
                .build();

        Page<Notification> notificationsPage = new PageImpl<>(List.of(notification));
        when(notificationRepository.findAllByUserId(eq(userId), any(PageRequest.class)))
                .thenReturn(notificationsPage);

        // Act
        Set<NotificationResponse> response = notificationService.listNotificationsByUserId(userId, PageRequest.of(0, 10));

        // Assert
        assertEquals(1, response.size());
        NotificationResponse notificationResponse = response.iterator().next();
        assertEquals("Test Notification", notificationResponse.getContent());
        assertEquals(userId, notificationResponse.getUserId());

        // Verify
        verify(notificationRepository).findAllByUserId(eq(userId), any(PageRequest.class));
    }

    @Test
    @DisplayName("Test: Create a Notification Successfully with Valid Data")
    public void shouldCreateNotificationSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setId(1L);
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        NotificationRequest request = NotificationRequest.builder()
                .date(LocalDateTime.now())
                .type(NotificationType.INFO)
                .content("New Notification")
                .isRead(false)
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .date(request.getDate())
                .type(request.getType())
                .content(request.getContent())
                .isRead(request.isRead())
                .user(patient)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        NotificationResponse response = notificationService.createNotification(request, patient);

        // Assert
        assertEquals("New Notification", response.getContent());
        assertEquals(patient.getId(), response.getUserId());
        assertFalse(response.isRead());

        // Verify
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Test: Update Notification Successfully")
    public void shouldUpdateNotificationSuccessfully_whenValidDataIsProvided() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setId(1L);
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        NotificationRequest request = NotificationRequest.builder()
                .userId(1L)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Updated Content")
                .isRead(true)
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .content("Old Content")
                .isRead(false)
                .user(patient)
                .build();

        when(notificationRepository.save(notification)).thenReturn(notification);

        // Act
        NotificationResponse response = notificationService.updateNotification(request, notification);

        LocalDateTime responseDate = LocalDateTime.parse(response.getDate(), formatter);

        // Assert
        assertEquals(request.getDate().truncatedTo(ChronoUnit.SECONDS), responseDate);

        // Verify
        verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("Test: Delete Notification Successfully")
    public void shouldDeleteNotificationSuccessfully_whenNotificationExists() {
        // Arrange
        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setId(1L);
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        Notification notification = Notification.builder()
                .id(1L)
                .content("Content to be deleted")
                .isRead(false)
                .user(patient)
                .build();

        doNothing().when(notificationRepository).delete(notification);

        // Act
        notificationService.deleteNotification(notification);

        // Verify
        verify(notificationRepository).delete(notification);
    }

    @Test
    @DisplayName("Test: Return Notification Successfully when found by ID")
    public void shouldReturnNotificationSuccessfully_whenNotificationIsFoundById() {
        // Arrange
        Long notificationId = 1L;
        Patient patient = Patient.builder()
                .address("Mitre 123")
                .build();
        patient.setId(1L);
        patient.setFirstName("Matías");
        patient.setLastName("Clauss");
        patient.setEmail("matiasclauss@mail.com");

        Notification notification = Notification.builder()
                .id(notificationId)
                .content("Content")
                .isRead(true)
                .user(patient)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // Act
        Notification result = notificationService.getNotificationById(notificationId);

        // Assert
        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        assertEquals("Content", result.getContent());

        // Verify
        verify(notificationRepository).findById(notificationId);
    }

    @Test
    @DisplayName("Test: Throw NotificationNotFoundException when Notification not found by ID")
    public void shouldThrowNotificationNotFoundException_whenNotificationIsNotFoundById() {
        // Arrange
        Long notificationId = 1L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        NotificationNotFoundException exception = assertThrows(NotificationNotFoundException.class, () -> {
            notificationService.getNotificationById(notificationId);
        });

        assertEquals("Notification with ID 1 does not exist.", exception.getMessage());

        // Verify
        verify(notificationRepository).findById(notificationId);
    }

}
