package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.configuration.CacheConfiguration;
import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.domain.GenreRating;
import com.iamjunhyeok.MovieNotifier.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ContextConfiguration(classes = CacheConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    private List<GenreRating> genreRatings;

    private User user;

    private LocalTime notificationTime = LocalTime.of(17, 0);

    @BeforeEach
    void setup() {
        GenreRating genreRating1 = new GenreRating(Genre.ACTION, 7);
        GenreRating genreRating2 = new GenreRating(Genre.HORROR, 6.5F);
        testEntityManager.persist(genreRating1);
        testEntityManager.persist(genreRating2);
        genreRatings = new ArrayList<>(Arrays.asList(genreRating1, genreRating2));

        user = new User("jeon", "1231231", "01012345678");
        user.setConditions(genreRatings);
        user.changeNotificationTime(notificationTime);
        testEntityManager.persist(user);
    }

    @Test
    @DisplayName("알림 시간으로 사용자 목록 조회")
    void testFindUsersByNotificationTime() {
        // Arrange

        // Act
        List<User> users = userRepository.findAllByNotificationTime(notificationTime);

        // Assert
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        assertEquals(2, users.get(0).getGenreRatings().size());
        assertEquals(genreRatings.get(0), users.get(0).getGenreRatings().get(0));
        assertEquals(genreRatings.get(1), users.get(0).getGenreRatings().get(1));
    }

    @Test
    @DisplayName("캐시를 이용한 사용자 목록 조회")
    void testFindUsersUsingCache() {
        // Arrange

        // Act
        List<User> firstCall = userRepository.findAllByNotificationTime(notificationTime);
        List<User> secondCall = userRepository.findAllByNotificationTime(notificationTime);

        // Assert
        assertEquals(firstCall.size(), secondCall.size());
        assertNotNull(cacheManager.getCache("users").get(notificationTime));
    }

    @Test
    @DisplayName("사용자 정보 변경 시 기존 캐시 제거")
    void EvictCacheOnUserUpdate() {
        // Arrange
        userRepository.findAllByNotificationTime(notificationTime);

        // Act
        userRepository.save(user);

        // Assert
        assertNull(cacheManager.getCache("users").get(notificationTime));
    }
}