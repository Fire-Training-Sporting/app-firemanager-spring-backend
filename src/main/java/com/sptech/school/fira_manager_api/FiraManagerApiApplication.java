package com.sptech.school.fira_manager_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class FiraManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiraManagerApiApplication.class, args);
    }

}