package com.sharex.token.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TokenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenApiApplication.class, args);
	}
}
