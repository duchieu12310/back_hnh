package com.hnh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HnhApplication {

    public static void main(String[] args) {
        SpringApplication.run(HnhApplication.class, args);
    }

}

