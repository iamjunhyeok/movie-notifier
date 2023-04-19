package com.iamjunhyeok.MovieNotifier.batch;

import com.iamjunhyeok.MovieNotifier.constant.Genre;
import com.iamjunhyeok.MovieNotifier.domain.Actor;
import com.iamjunhyeok.MovieNotifier.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WebCrawlingItemReader implements ItemReader<Movie> {

    private RestTemplate restTemplate = new RestTemplate();

    private List<Movie> movies = null;

    @Value("${movie.url}")
    private String movieUrl;

    @Value("${movie.queryParams.key}")
    private String key;

    @Value("${movie.queryParams.pkid}")
    private String pkid;

    @Value("${movie.queryParams.where}")
    private String where;

    @Value("${movie.queryParams.start}")
    private String start;

    @Value("${movie.queryParams.display}")
    private String display;

    @Value("${movie.queryParams.sort}")
    private String sort;

    @Value("${movie.queryParams.query}")
    private String query;

    private static final Map<String, Genre> genreMap = new HashMap<>();

    static {
        genreMap.put("액션", Genre.ACTION);
        genreMap.put("모험", Genre.ADVENTURE);
        genreMap.put("애니메이션", Genre.ANIMATION);
        genreMap.put("전기", Genre.BIOGRAPHY);
        genreMap.put("코미디", Genre.COMEDY);
        genreMap.put("범죄", Genre.CRIME);
        genreMap.put("다큐멘터리", Genre.DOCUMENTARY);
        genreMap.put("드라마", Genre.DRAMA);
        genreMap.put("가족", Genre.FAMILY);
        genreMap.put("판타지", Genre.FANTASY);
        genreMap.put("역사", Genre.HISTORY);
        genreMap.put("공포", Genre.HORROR);
        genreMap.put("공연실황", Genre.MUSIC);
        genreMap.put("뮤지컬", Genre.MUSICAL);
        genreMap.put("미스터리", Genre.MYSTERY);
        genreMap.put("멜로/로맨스", Genre.ROMANCE);
        genreMap.put("SF", Genre.SF);
        genreMap.put("스포츠", Genre.SPORT);
        genreMap.put("스릴러", Genre.THRILLER);
        genreMap.put("전쟁", Genre.WAR);
        genreMap.put("서부", Genre.WESTERN);
    }

    public void init() {
        movies = null;
    }

    @Override
    public Movie read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (movies == null) {
            log.info("크롤링 시작!!");
            movies = new ArrayList<>();

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(getMovieUri(), String.class);
            JSONObject body = new JSONObject(responseEntity.getBody());
            JSONArray items = (JSONArray) body.get("items");
            JSONObject item = items.getJSONObject(0);

            String html = item.getString("html");
            Document parse = Jsoup.parse(html);
            Elements elements = parse.select("div.data_area");
            for (int i = 0; i < elements.size(); i++) {
                Element dataArea = elements.get(i);
                Movie movie = parseMovieElement(dataArea);
                movies.add(movie);
            }
            log.info("크롤링 완료!!");
        }
        if (!movies.isEmpty()) return movies.remove(0);
        return null;
    }

    private String getMovieUri() {
        return UriComponentsBuilder.fromHttpUrl(movieUrl)
                .queryParam("key", key)
                .queryParam("pkid", pkid)
                .queryParam("where", where)
                .queryParam("start", start)
                .queryParam("display", display)
                .queryParam("so", sort)
                .queryParam("q", query)
                .build()
                .toUriString();
    }

    private Movie parseMovieElement(Element dataArea) {
        Elements titleElement = dataArea.select("div.title._ellipsis > div > a");

        Elements info = dataArea.select("div.info");
        Elements genreElement = info.select("dl:nth-child(1) > dd:nth-child(2)");
        Elements runningTimeElement = info.select("dl:nth-child(1) > dd:nth-child(3)");
        Elements releaseDateElement = info.select("dl:nth-child(2) > dd:nth-child(2)");
        Elements ratingElement = info.select("dl:nth-child(2) > dd:nth-child(4) > span");
        Elements actorElement = info.select("dl:nth-child(3) > dd > span");
        String[] split = actorElement.text().split(", ");

        List<Actor> actors = Arrays.stream(split)
                .map(Actor::new)
                .toList();

        Long id = extractId(dataArea);
        String title = titleElement.text();
        Genre genre = genreMap.getOrDefault(genreElement.text(), Genre.ETC);
        int runningTime = Integer.parseInt(runningTimeElement.text().replace("분", ""));
        String[] releaseDate = releaseDateElement.text().split("\\.");
        LocalDate releaseLocalDate = LocalDate.of(Integer.parseInt(releaseDate[0]), Integer.parseInt(releaseDate[1]), Integer.parseInt(releaseDate[2]));
        float rating = Float.parseFloat(ratingElement.text());

        Movie movie = new Movie(id, title, releaseLocalDate, runningTime, genre, rating);
        movie.casting(actors);
        return movie;
    }

    private Long extractId(Element dataArea) {
        Elements titleElement = dataArea.select("div.title._ellipsis > div > a");
        Pattern pattern = Pattern.compile("os=(\\d+)");
        Matcher matcher = pattern.matcher(titleElement.outerHtml());
        if (matcher.find()) {
            return Long.valueOf(matcher.group(1));
        }
        return null;
    }
}
