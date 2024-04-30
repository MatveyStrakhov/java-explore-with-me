package ru.practicum.ewm.stats.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EWMStatsServerApp {
    public static void main(String[] args) {
        SpringApplication.run(EWMStatsServerApp.class, args);
    }
}
