package com.iamjunhyeok.MovieNotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MovieNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieNotifierApplication.class, args);
	}

}
