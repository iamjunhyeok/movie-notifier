package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class SendNotificationItemWriter implements ItemWriter<Movie> {

    @Override
    public void write(Chunk<? extends Movie> chunk) throws Exception {

    }
}
