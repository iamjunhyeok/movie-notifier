package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.GenreRating;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.service.NotificationService;
import com.iamjunhyeok.MovieNotifier.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FilterByUserItemProcessor implements ItemProcessor<Movie, Map<Movie, List<User>>> {

    private final UserService userService;

    private final NotificationService notificationService;

    @Value("#{jobParameters['timestamp']}")
    private LocalTime timestamp;

    @Override
    public Map<Movie, List<User>> process(Movie item) throws Exception {
        List<User> users = userService.getUsersByNotificationTime(timestamp);
        List<User> matchedUsers = new ArrayList<>();
        for (User user : users) {
            for (GenreRating genreRating : user.getGenreRatings()) {
                if (item.getGenre().equals(genreRating.getGenre())
                        && item.getRating() >= genreRating.getRating()
                        && !notificationService.isAlreadySent(item, user)) {
                    matchedUsers.add(user);
                }
            }
        }
        if (!matchedUsers.isEmpty()) {
            Map<Movie, List<User>> movieListMap = new HashMap<>();
            movieListMap.put(item, matchedUsers);
            return movieListMap;
        }
        return null;
    }
}
