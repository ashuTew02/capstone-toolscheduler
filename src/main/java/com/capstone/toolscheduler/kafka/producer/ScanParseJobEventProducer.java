package com.capstone.toolscheduler.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.capstone.toolscheduler.dto.event.ScanParseJobEvent;
import com.capstone.toolscheduler.dto.event.payload.ScanParseJobEventPayload;
import com.capstone.toolscheduler.model.Tool;
import com.capstone.toolscheduler.model.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScanParseJobEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ScanParseJobEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                     ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void produce(Tool tool, String filePath, Long tenantId) {
        try {
            ScanParseJobEventPayload payload = new ScanParseJobEventPayload(tool, tenantId, filePath);
            ScanParseJobEvent event = new ScanParseJobEvent(payload);

            String eventJson = objectMapper.writeValueAsString(event);

            // Send the parse job event to JFC
            kafkaTemplate.send(KafkaTopic.TOOLSCHEDULER_JFC.getTopicName(),
                               event.getEventId(),
                               eventJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
