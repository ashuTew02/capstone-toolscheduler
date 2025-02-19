package com.capstone.toolscheduler.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.capstone.toolscheduler.dto.event.ScanParseEvent;
import com.capstone.toolscheduler.dto.event.payload.ScanParseEventPayload;
import com.capstone.toolscheduler.model.Tool;
import com.capstone.toolscheduler.model.KafkaTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScanParseEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ScanParseEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                     ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void produce(Tool tool, String filePath, Long tenantId) {
        try {
            ScanParseEventPayload payload = new ScanParseEventPayload(tool, tenantId, filePath, KafkaTopic.PARSER_JFC);
            ScanParseEvent event = new ScanParseEvent(payload);

            String eventJson = objectMapper.writeValueAsString(event);

            // Send the parse job event to JFC
            kafkaTemplate.send(KafkaTopic.JOBINGESTION_JFC.getTopicName(),
                               event.getEventId(),
                               eventJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
