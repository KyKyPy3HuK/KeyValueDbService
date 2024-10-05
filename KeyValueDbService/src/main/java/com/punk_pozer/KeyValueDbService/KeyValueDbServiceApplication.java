package com.punk_pozer.KeyValueDbService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeyValueDbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyValueDbServiceApplication.class, args);
	}

}
