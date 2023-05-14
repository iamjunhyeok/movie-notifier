package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.repository.NotificationRepository;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private DefaultMessageService messageService;

    private User user;

    private Movie movie;

    @BeforeEach
    void setup() {
        user = new User("jeon", "1231231", "01012345678");
        movie = new Movie(1L, "무서운영화", LocalDate.of(2023, 5, 11), 90, Genre.HORROR, 8.5F);
    }

    @Test
    @DisplayName("해당 영화의 알림이 발송되었는지 확인")
    void testCheckMovieNotificationSent() {
        // Arrange
        when(notificationRepository.existsByUserAndMovie(user, movie)).thenReturn(true);

        // Act & Assert
        assertTrue(notificationService.isAlreadySent(movie, user));
    }

    @Test
    @DisplayName("해당 영화의 알림이 발송되지 않았는지 확인")
    void testCheckMovieNotificationNotSent() {
        // Arrange
        when(notificationRepository.existsByUserAndMovie(user, movie)).thenReturn(false);

        // Act & Assert
        assertFalse(notificationService.isAlreadySent(movie, user));
    }

    @Test
    @DisplayName("알림을 발송")
    void testSendMovieNotification() {
        // Arrange
        Notification notification = new Notification(user, movie);
        when(notificationRepository.findAllByStatus(any())).thenReturn(Arrays.asList(notification));
        when(messageService.sendOne(any())).thenReturn(mock(SingleMessageSentResponse.class));

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.sendNotification());
    }
}