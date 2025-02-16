package com.capstone.toolscheduler.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.capstone.toolscheduler.dto.event.AckScanRequestJobEvent;
import com.capstone.toolscheduler.dto.event.payload.AckJobEventPayload;
import com.capstone.toolscheduler.model.KafkaTopic;
import com.capstone.toolscheduler.model.JobStatus;  // or import from your Enum
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AckScanRequestJobEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AckScanRequestJobEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                          ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void produce(String jobId) {
        try {
            // Mark job as SUCCESS or IN_PROGRESS, etc. For now, let's do SUCCESS.
            AckJobEventPayload payload = new AckJobEventPayload(jobId, JobStatus.SUCCESS);
            AckScanRequestJobEvent ackEvent = new AckScanRequestJobEvent(payload);

            String json = objectMapper.writeValueAsString(ackEvent);

            kafkaTemplate.send(KafkaTopic.ACK_JOB.getTopicName(),
                               jobId,
                               json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
