package com.iamjunhyeok.MovieNotifier.domain;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@Getter
@Entity
public class Movie extends DateTime {
    @Id
    @Column(name = "movie_id", nullable = false)
    private Long id;

    private String title;

    private LocalDate releaseDate;

    private String certification;

    private int runningTime;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private float rating;

    private String storyline;

    public Movie(Long id, String title, LocalDate releaseDate, int runningTime, Genre genre, float rating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.runningTime = runningTime;
        this.genre = genre;
        this.rating = rating;
    }
}
