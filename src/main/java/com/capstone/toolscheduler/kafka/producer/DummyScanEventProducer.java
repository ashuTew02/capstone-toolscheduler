package com.capstone.toolscheduler.kafka.producer;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.dto.event.payload.ScanRequestJobEventPayload;
import com.capstone.toolscheduler.model.Tool;

@Component
public class DummyScanEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.scan-request}")
    private String scanRequestTopic;

    public DummyScanEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceDummyEvent() {
        // ScanRequestJobEventPayload event = new ScanRequestJobEventPayload("ashuTew01", "juice-shop", Arrays.asList(Tool.CODE_SCAN), 1L);
        // kafkaTemplate.send(scanRequestTopic, event);
        System.out.println("Dummy scanRequestEvent published to topic: " + scanRequestTopic);
    }
}
