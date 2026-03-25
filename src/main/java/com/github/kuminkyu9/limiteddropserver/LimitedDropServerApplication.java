package com.github.kuminkyu9.limiteddropserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LimitedDropServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimitedDropServerApplication.class, args);
	}

}
