package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SendNotificationItemWriter implements ItemWriter<Map<Movie, List<User>>> {

    private final NotificationService notificationService;

    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {
        for (Map<Movie, List<User>> movieListMap : chunk) {
            for (Map.Entry<Movie, List<User>> movieListEntry : movieListMap.entrySet()) {
                Movie movie = movieListEntry.getKey();
                List<User> users = movieListEntry.getValue();
                notificationService.sendNotification(movie, users);
            }
        }
    }
}
