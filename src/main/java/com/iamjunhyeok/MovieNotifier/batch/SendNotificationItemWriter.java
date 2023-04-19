package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;

public class SendNotificationItemWriter implements ItemWriter<Map<Movie, List<User>>> {
    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {

    }
}
