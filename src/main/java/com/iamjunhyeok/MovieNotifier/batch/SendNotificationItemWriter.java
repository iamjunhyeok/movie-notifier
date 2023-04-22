package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.Notification;
import com.iamjunhyeok.MovieNotifier.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SendNotificationItemWriter implements ItemWriter<Map<Movie, List<User>>> {

    private final JpaItemWriter<Notification> jpaItemWriter;

    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {
        List<Notification> notifications = new ArrayList<>();
        for (Map<Movie, List<User>> movieListMap : chunk) {
            for (Map.Entry<Movie, List<User>> movieListEntry : movieListMap.entrySet()) {
                Movie movie = movieListEntry.getKey();
                List<User> users = movieListEntry.getValue();
                for (User user : users) {
                    Notification notification = new Notification(user, movie);
                    notifications.add(notification);
                }
            }
        }
        jpaItemWriter.write(new Chunk<>(notifications));
    }
}
