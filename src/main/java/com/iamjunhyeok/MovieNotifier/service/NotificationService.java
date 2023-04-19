package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendNotification(Movie movie, List<User> users) {
        for (User user : users) {
            Notification notification = new Notification(getMessage(movie), user, movie);
            notificationRepository.save(notification);
        }
    }

    public String getMessage(Movie movie) {
        return movie.toString();
    }
}
