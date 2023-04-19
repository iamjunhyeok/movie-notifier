package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users")
    @EntityGraph(attributePaths = "genreRatings")
    List<User> findAll();

    @CacheEvict(value = "users")
    <S extends User> S save(S entity);
}
