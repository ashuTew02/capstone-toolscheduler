package com.capstone.toolscheduler.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.capstone.toolscheduler.dto.event.ScanJobEvent;
import com.capstone.toolscheduler.model.ScanType;

@Service
public class ScanJobEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.scan-job}")
    private String scanJobEventTopic;

    public ScanJobEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(ScanType scanType, String filePath, String esIndexOfFindings) {
        ScanJobEvent event = new ScanJobEvent(scanType.getValue(), filePath, esIndexOfFindings);
        kafkaTemplate.send(scanJobEventTopic, event);
    }
}
