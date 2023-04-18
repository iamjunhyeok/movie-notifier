package com.iamjunhyeok.MovieNotifier.repository;

import com.iamjunhyeok.MovieNotifier.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserId(Long userId);
}
