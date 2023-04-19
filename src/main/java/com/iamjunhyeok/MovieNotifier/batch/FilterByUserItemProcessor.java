package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.GenreRating;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.service.NotificationService;
import com.iamjunhyeok.MovieNotifier.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FilterByUserItemProcessor implements ItemProcessor<Movie, Map<Movie, List<User>>> {

    private final UserService userService;

    private final NotificationService notificationService;

    @Override
    public Map<Movie, List<User>> process(Movie item) throws Exception {
        List<User> users = userService.getUsers();
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
        Map<Movie, List<User>> map = new HashMap<>();
        map.put(item, matchedUsers);
        return map;
    }
}
