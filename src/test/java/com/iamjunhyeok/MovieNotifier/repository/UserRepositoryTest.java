package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.domain.GenreRating;
import com.iamjunhyeok.MovieNotifier.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("알림 시간으로 사용자 목록 조회")
    void testFindAllByNotificationTime() {
        // Arrange
        GenreRating genreRating1 = new GenreRating(Genre.ACTION, 7);
        GenreRating genreRating2 = new GenreRating(Genre.HORROR, 6.5F);
        testEntityManager.persist(genreRating1);
        testEntityManager.persist(genreRating2);

        User user = new User("jeon", "1231231", "01012345678");
        user.setConditions(Arrays.asList(genreRating1, genreRating2));
        user.changeNotificationTime(LocalTime.of(17, 0));
        testEntityManager.persist(user);

        // Act
        List<User> users = userRepository.findAllByNotificationTime(LocalTime.of(17, 0));

        // Assert
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        assertEquals(2, users.get(0).getGenreRatings().size());
        assertEquals(genreRating1, users.get(0).getGenreRatings().get(0));
        assertEquals(genreRating2, users.get(0).getGenreRatings().get(1));
    }
}