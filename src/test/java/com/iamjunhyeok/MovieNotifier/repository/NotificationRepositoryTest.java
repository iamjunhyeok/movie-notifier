package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.constant.NotificationStatus;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
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

    @Test
    @DisplayName("사용자에게 발송된 알림 목록 조회")
    void testFindAllByUserId() {
        // Arrange
        User user = new User("jeon", "1231231", "01012345678");
        testEntityManager.persist(user);

        Movie movie = new Movie(1L, "무서운영화", LocalDate.of(2023, 5, 11), 90, Genre.HORROR, 8.5F);
        testEntityManager.persist(movie);

        Notification notification = new Notification(user, movie);
        testEntityManager.persist(notification);

        // Act
        List<Notification> result = notificationRepository.findAllByUserId(user.getId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUser().getId());
    }

    @Test
    @DisplayName("사용자에게 해당 영화의 알림이 발송된적이 있는지")
    void testExistsByUserAndMovie() {
        // Arrange
        User user = new User("jeon", "1231231", "01012345678");
        testEntityManager.persist(user);

        Movie movie = new Movie(1L, "무서운영화", LocalDate.of(2023, 5, 11), 90, Genre.HORROR, 8.5F);
        testEntityManager.persist(movie);

        Notification notification = new Notification(user, movie);
        testEntityManager.persist(notification);

        // Act & Assert
        assertTrue(notificationRepository.existsByUserAndMovie(user, movie));
    }

    @Test
    @DisplayName("상태로 알림 목록 조회")
    void testFindAllByStatus() {
        // Arrange
        User user = new User("jeon", "1231231", "01012345678");
        testEntityManager.persist(user);

        Movie movie = new Movie(1L, "무서운영화", LocalDate.of(2023, 5, 11), 90, Genre.HORROR, 8.5F);
        testEntityManager.persist(movie);

        Notification notification = new Notification(user, movie);
        notification.complete();
        testEntityManager.persist(notification);

        // Act
        List<Notification> result = notificationRepository.findAllByStatus(NotificationStatus.COMPLETE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(NotificationStatus.COMPLETE, result.get(0).getStatus());
    }
}