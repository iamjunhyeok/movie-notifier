package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.constant.NotificationStatus;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    private User user;

    private Movie movie;

    private Notification notification;

    @BeforeEach
    void setup() {
        user = new User("jeon", "1231231", "01012345678");
        testEntityManager.persist(user);

        movie = new Movie(1L, "무서운영화", LocalDate.of(2023, 5, 11), 90, Genre.HORROR, 8.5F);
        testEntityManager.persist(movie);

        notification = new Notification(user, movie);
        testEntityManager.persist(notification);
    }

    @Test
    @DisplayName("사용자에게 해당 영화의 알림 발송 여부 확인")
    void testCheckIfMovieNotificationWasSentToUser() {
        // Arrange

        // Act & Assert
        assertTrue(notificationRepository.existsByUserAndMovie(user, movie));
    }

    @Test
    @DisplayName("알림 상태로 알림 목록 조회")
    void testFindNotificationsByStatus() {
        // Arrange
        notification.complete();
        testEntityManager.persist(notification);

        // Act
        List<Notification> result = notificationRepository.findAllByStatus(NotificationStatus.COMPLETE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(NotificationStatus.COMPLETE, result.get(0).getStatus());
    }
}