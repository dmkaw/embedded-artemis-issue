package com.dmkaw.artemis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dmkaw.artemis")
public class EmbeddedArtemis {

    public static void main(String[] args) {
        SpringApplication.run(EmbeddedArtemis.class, args);
    }

}
