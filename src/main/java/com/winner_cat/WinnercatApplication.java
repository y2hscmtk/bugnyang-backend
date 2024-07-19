package com.winner_cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WinnercatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WinnercatApplication.class, args);
	}

}
