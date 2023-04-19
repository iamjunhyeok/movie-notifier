package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import org.springframework.batch.item.ItemProcessor;

public class FilterByUserItemProcessor implements ItemProcessor<Movie, Movie> {
    @Override
    public Movie process(Movie item) throws Exception {
        return null;
    }

}
