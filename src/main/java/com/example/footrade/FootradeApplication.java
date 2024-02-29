package com.example.footrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("file:./.env")
public class FootradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootradeApplication.class, args);
    }

}
