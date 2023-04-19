package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class ChainedItemWriter implements ItemWriter<Movie> {

    private final DataStorageItemWriter dataStorageItemWriter;

    private final SendNotificationItemWriter sendNotificationItemWriter;

    @Override
    public void write(Chunk<? extends Movie> chunk) throws Exception {
        dataStorageItemWriter.write(chunk);
        sendNotificationItemWriter.write(chunk);
    }
}
