package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DataStorageItemWriter implements ItemWriter<Map<Movie, List<User>>> {

    private final JpaItemWriter<Movie> jpaItemWriter;

    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {
        List<Movie> movies = new ArrayList<>();
        for (Map<Movie, List<User>> movieListMap : chunk) {
            movies.addAll(movieListMap.keySet());
        }
        jpaItemWriter.write(new Chunk<>(movies));
    }
}
