package com.capstone.toolscheduler.runner;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.kafka.producer.DummyScanEventProducer;

@Component
public class StartupRunner implements CommandLineRunner {

    private final DummyScanEventProducer producer;

    public StartupRunner(DummyScanEventProducer producer) {
        this.producer = producer;
    }

    @Override
    public void run(String... args) {
        // producer.produceDummyEvent();
    }
}