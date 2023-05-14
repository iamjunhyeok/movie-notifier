package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users")
    @Query("select u from User u join fetch u.genreRatings gr where u.notificationTime = :localTime")
    List<User> findAllByNotificationTime(LocalTime localTime);

    @CacheEvict(value = "users", allEntries = true)
    <S extends User> S save(S entity);
}
