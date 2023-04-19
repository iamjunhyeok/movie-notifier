package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class WebCrawlingItemReader implements ItemReader<Movie> {
    @Override
    public Movie read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

}
