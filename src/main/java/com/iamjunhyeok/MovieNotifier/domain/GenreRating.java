package com.iamjunhyeok.MovieNotifier.domain;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
