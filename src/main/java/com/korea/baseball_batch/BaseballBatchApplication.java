package com.korea.baseball_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BaseballBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseballBatchApplication.class, args);
	}

}
