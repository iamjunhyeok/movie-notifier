package com.iamjunhyeok.MovieNotifier.constant;

import java.util.HashMap;
import java.util.Map;

public enum Genre {
    ACTION("액션"),
    ADVENTURE("모험"),
    ANIMATION("애니메이션"),
    BIOGRAPHY("전기"),
    COMEDY("코미디"),
    CRIME("범죄"),
    DOCUMENTARY("다큐멘터리"),
    DRAMA("드라마"),
    FAMILY("가족"),
    FANTASY("판타지"),
    HISTORY("역사"),
    HORROR("공포"),
    MUSIC("공연실황"),
    MUSICAL("뮤지컬"),
    MYSTERY("미스터리"),
    ROMANCE("멜로/로맨스"),
    SF("SF"),
    SPORT("스포츠"),
    THRILLER("스릴러"),
    WAR("전쟁"),
    WESTERN("서부"),
    ETC("기타");

    private final String value;

    Genre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, Genre> GENRE_MAP = new HashMap<>();

    static {
        for (Genre genre : values()) {
            GENRE_MAP.put(genre.value, genre);
        }
    }

    public static Genre getKeyByValue(String value) {
        return GENRE_MAP.getOrDefault(value, Genre.ETC);
    }
}