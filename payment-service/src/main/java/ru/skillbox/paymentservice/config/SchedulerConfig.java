package ru.skillbox.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
public class SchedulerConfig {
    @Bean
    public Scheduler getScheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(1));
    }
}
