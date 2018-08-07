package com.sharex.token.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@EnableScheduling
@SpringBootApplication
public class TokenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenApiApplication.class, args);
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor(){
		return new MethodValidationPostProcessor();
	}
}
