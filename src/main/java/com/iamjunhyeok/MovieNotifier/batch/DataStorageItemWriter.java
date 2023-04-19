package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.domain.User;
import com.iamjunhyeok.MovieNotifier.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DataStorageItemWriter implements ItemWriter<Map<Movie, List<User>>> {

    private final MovieService movieService;

    @Override
    public void write(Chunk<? extends Map<Movie, List<User>>> chunk) throws Exception {
        for (Map<Movie, List<User>> movieListMap : chunk) {
            List<Movie> movies = new ArrayList<>(movieListMap.keySet());
            movieService.save(movies);
        }
    }
}
