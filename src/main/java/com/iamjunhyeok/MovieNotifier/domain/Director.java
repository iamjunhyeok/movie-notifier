package com.iamjunhyeok.MovieNotifier.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "director_id", nullable = false)
    private Long id;

    private String name;

    private int orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
