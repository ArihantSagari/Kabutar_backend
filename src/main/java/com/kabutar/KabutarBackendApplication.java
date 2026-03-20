package com.kabutar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
public class KabutarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KabutarBackendApplication.class, args);
    }
}