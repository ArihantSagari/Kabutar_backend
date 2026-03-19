package com.kabutar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class KabutarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KabutarBackendApplication.class, args);
	}

}
