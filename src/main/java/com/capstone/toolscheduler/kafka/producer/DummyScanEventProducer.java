package com.capstone.toolscheduler.kafka.producer;


import java.util.Arrays;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.dto.event.ScanRequestEvent;
import com.capstone.toolscheduler.model.ScanType;

@Component
public class DummyScanEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // @Value("${kafka.topics.scan-request}")
    private final String scanRequestTopic = "scan-request";

    public DummyScanEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceDummyEvent() {

        ScanRequestEvent event = new ScanRequestEvent("ashuTew01", "juice-shop", Arrays.asList(ScanType.ALL));

        kafkaTemplate.send(scanRequestTopic, event);
        System.out.println("Dummy scanRequestEvent published to topic: " + scanRequestTopic);
    }
}
