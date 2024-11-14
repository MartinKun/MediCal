package com.app.controller;

import com.app.common.enums.NotificationType;
import com.app.controller.dto.request.NotificationRequest;
import com.app.controller.dto.response.NotificationResponse;
import com.app.exception.NotificationNotFoundException;
import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Notification;
import com.app.persistence.entity.User;
import com.app.resolvers.CustomUsernameArgumentResolver;
import com.app.service.implementation.NotificationServiceImpl;
import com.app.service.implementation.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationServiceImpl notificationService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "testUser@mail.com"; //The username replaces the @AuthenticationPrincipal parameter from CustomUsernameArgumentResolver.java with a predefined value for the test.

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .setCustomArgumentResolvers(new CustomUsernameArgumentResolver())
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("Test: Should list Notifications by User ID")
    void shouldListNotificationsByUserId_whenValidRequestIsMade() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        NotificationResponse notificationResponse = NotificationResponse.builder()
                .id(1L)
                .date("2024-11-13T18:02:48")
                .type(NotificationType.INFO)
                .content("Sample Notification")
                .isRead(false)
                .userId(myUser.getId())
                .build();

        Set<NotificationResponse> notifications = new HashSet<>();
        notifications.add(notificationResponse);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").descending());
        when(notificationService.listNotificationsByUserId(myUser.getId(), pageable)).thenReturn(notifications);

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/notifications")
                        .param("itemsByPage", "10")
                        .param("page", "0")
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2024-11-13T18:02:48"))
                .andExpect(jsonPath("$[0].type").value(NotificationType.INFO.name()))
                .andExpect(jsonPath("$[0].content").value("Sample Notification"))
                .andExpect(jsonPath("$[0].read").value(false))
                .andExpect(jsonPath("$[0].userId").value(myUser.getId()));
    }

    @Test
    @DisplayName("Test: Should return an empty list when no notifications are found for the user")
    void shouldReturnEmptyList_whenNoNotificationsExistForUser() throws Exception {
        // Arrange
        Doctor myUser = new Doctor();
        myUser.setId(1L);
        myUser.setEmail(username);
        when(userDetailService.loadUserByUsername(username)).thenReturn(myUser);

        Set<NotificationResponse> notifications = new HashSet<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").descending());
        when(notificationService.listNotificationsByUserId(myUser.getId(), pageable)).thenReturn(notifications);

        // Act & Assert
        this.mockMvc.perform(get("/api/v1/notifications")
                        .param("itemsByPage", "10")
                        .param("page", "0")
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Test: Should create a Notification successfully")
    void shouldCreateNotificationSuccessfully_whenValidDataIsProvided() throws Exception {
        // Arrange
        NotificationRequest request = NotificationRequest.builder()
                .userId(1L)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Notification Content")
                .isRead(false)
                .build();

        User user = new Doctor();
        user.setId(1L);
        user.setEmail("user@mail.com");

        when(userDetailService.getUserById(request.getUserId())).thenReturn(user);

        NotificationResponse notificationResponse = NotificationResponse.builder()
                .id(1L)
                .date(request.getDate().toString())
                .type(NotificationType.INFO)
                .content("Notification Content")
                .isRead(false)
                .userId(1L)
                .build();


        when(notificationService.createNotification(any(NotificationRequest.class), any(User.class))).thenReturn(notificationResponse);

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.date").value(request.getDate().toString()))
                .andExpect(jsonPath("$.type").value(NotificationType.INFO.toString()))
                .andExpect(jsonPath("$.content").value("Notification Content"))
                .andExpect(jsonPath("$.read").value(false))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @DisplayName("Test: Should update a Notification successfully when valid data is provided")
    void shouldUpdateNotificationSuccessfully_whenValidDataIsProvided() throws Exception {
        // Arrange
        NotificationRequest request = NotificationRequest.builder()
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Updated Notification Content")
                .isRead(true)
                .build();

        User myUser = new Doctor();
        myUser.setId(1L);
        myUser.setEmail(username);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        notification.setType(NotificationType.INFO);
        notification.setContent("Old Notification Content");
        notification.setRead(false);
        notification.setUser(myUser);

        NotificationResponse updatedNotificationResponse = NotificationResponse.builder()
                .id(1L)
                .date(request.getDate().toString())
                .type(NotificationType.INFO)
                .content("Updated Notification Content")
                .isRead(true)
                .userId(1L)
                .build();

        when(notificationService.getNotificationById(1L)).thenReturn(notification);
        when(notificationService.updateNotification(any(NotificationRequest.class), any(Notification.class)))
                .thenReturn(updatedNotificationResponse);

        // Act & Assert
        this.mockMvc.perform(put("/api/v1/notifications/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.date").value(request.getDate().toString()))
                .andExpect(jsonPath("$.type").value(NotificationType.INFO.toString()))
                .andExpect(jsonPath("$.content").value("Updated Notification Content"))
                .andExpect(jsonPath("$.read").value(true))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @DisplayName("Test: Should delete a Notification successfully when valid data is provided")
    void shouldDeleteNotificationSuccessfully_whenValidDataIsProvided() throws Exception {
        // Arrange
        User user = new Doctor();
        user.setId(1L);
        user.setEmail(username);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        notification.setType(NotificationType.INFO);
        notification.setContent("Notification to delete");
        notification.setRead(false);
        notification.setUser(user);

        when(notificationService.getNotificationById(1L)).thenReturn(notification);

        // Act & Assert
        this.mockMvc.perform(delete("/api/v1/notifications/{id}", 1L)
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Notification was deleted successfully."));
    }

    @Test
    @DisplayName("Test: Should throw UserDoesNotExistException when user does not exist")
    void shouldThrowUserDoesNotExistException_whenUserDoesNotExist() throws Exception {
        // Arrange
        NotificationRequest request = NotificationRequest.builder()
                .userId(1L)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Notification Content")
                .isRead(false)
                .build();

        when(userDetailService.getUserById(request.getUserId()))
                .thenThrow(new UserDoesNotExistException("User with ID 1 does not exist."));

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.error").value("User with ID 1 does not exist."));
    }

    @Test
    @DisplayName("Test: Should throw NotificationNotFoundException when notification does not exist (update Notification)")
    void shouldThrowNotificationNotFoundException_whenNotificationDoesNotExistInUpdate() throws Exception {
        // Arrange
        NotificationRequest request = NotificationRequest.builder()
                .userId(1L)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Notification Content")
                .isRead(false)
                .build();

        when(notificationService.getNotificationById(anyLong()))
                .thenThrow(new NotificationNotFoundException("Notification with ID 1 does not exist."));

        // Act & Assert
        this.mockMvc.perform(put("/api/v1/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.error").value("Notification with ID 1 does not exist."));
    }

    @Test
    @DisplayName("Test: Should throw NotificationAccessDeniedException when user has no permission to update notification (update Notification)")
    void shouldThrowNotificationAccessDeniedException_whenUserHasNoPermissionInUpdate() throws Exception {
        // Arrange
        NotificationRequest request = NotificationRequest.builder()
                .userId(2L)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(NotificationType.INFO)
                .content("Notification Content")
                .isRead(false)
                .build();

        User user = new Doctor();
        user.setId(1L);
        user.setEmail("user@mail.com");

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);

        when(notificationService.getNotificationById(1L)).thenReturn(notification);

        // Act & Assert
        this.mockMvc.perform(put("/api/v1/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors.error").value("You do not have permission to update this appointment."));
    }

    @Test
    @DisplayName("Test: Should throw NotificationNotFoundException when notification does not exist (delete Notification)")
    void shouldThrowNotificationNotFoundException_whenNotificationDoesNotExistInDelete() throws Exception {
        // Arrange
        when(notificationService.getNotificationById(anyLong()))
                .thenThrow(new NotificationNotFoundException("Notification with ID 1 does not exist."));

        // Act & Assert
        this.mockMvc.perform(delete("/api/v1/notifications/1")
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.error").value("Notification with ID 1 does not exist."));
    }

    @Test
    @DisplayName("Test: Should throw NotificationAccessDeniedException when user has no permission to delete notification")
    void shouldThrowNotificationAccessDeniedException_whenUserHasNoPermissionInDelete() throws Exception {
        // Arrange
        User user = new Doctor();
        user.setId(1L);
        user.setEmail("user@mail.com");
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);

        when(notificationService.getNotificationById(1L)).thenReturn(notification);

        // Act & Assert
        this.mockMvc.perform(delete("/api/v1/notifications/1")
                        .header("Authorization", "Bearer token"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors.error").value("You do not have permission to delete this appointment."));
    }

}
