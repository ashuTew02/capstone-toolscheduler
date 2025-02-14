package com.capstone.toolscheduler.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.capstone.toolscheduler.dto.event.ScanParseJobEvent;
import com.capstone.toolscheduler.dto.event.payload.ScanParseJobEventPayload;
import com.capstone.toolscheduler.model.Tool;

@Service
public class ScanJobEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.scan-job}")
    private String scanJobEventTopic;

    public ScanJobEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(Tool tool, String filePath, Long tenantId) {
        ScanParseJobEventPayload payload = new ScanParseJobEventPayload(tool, tenantId, filePath);
        ScanParseJobEvent event = new ScanParseJobEvent(payload);
        kafkaTemplate.send(scanJobEventTopic, event);
    }
}
