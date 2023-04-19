package com.iamjunhyeok.MovieNotifier.service;

import com.iamjunhyeok.MovieNotifier.domain.Movie;
import com.iamjunhyeok.MovieNotifier.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public void save(List<Movie> movies) {
        movieRepository.saveAll(movies);
        movieRepository.flush();
    }
}
