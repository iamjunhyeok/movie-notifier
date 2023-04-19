package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ChainedItemWriter implements ItemWriter<Map<Movie, List<User>>> {

    private final DataStorageItemWriter dataStorageItemWriter;

    private final SendNotificationItemWriter sendNotificationItemWriter;

    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {
        dataStorageItemWriter.write(chunk);
        sendNotificationItemWriter.write(chunk);
    }
}
