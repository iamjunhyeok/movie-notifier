package com.iamjunhyeok.MovieNotifier.domain;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class GenreRating extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "genre_rating_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public GenreRating(Genre genre, float rating) {
        this.genre = genre;
        this.rating = rating;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
