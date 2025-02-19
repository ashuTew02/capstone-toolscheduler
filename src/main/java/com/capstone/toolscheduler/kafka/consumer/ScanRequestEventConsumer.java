package com.capstone.toolscheduler.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.dto.event.job.ScanRequestJobEvent;
import com.capstone.toolscheduler.dto.event.payload.job.ScanRequestJobEventPayload;
import com.capstone.toolscheduler.model.JobStatus;
import com.capstone.toolscheduler.model.Tool;
import com.capstone.toolscheduler.repository.TenantRepository;
import com.capstone.toolscheduler.service.CodeScanRequestHandlerService;
import com.capstone.toolscheduler.service.DependabotScanRequestHandlerService;
import com.capstone.toolscheduler.service.SecretScanRequestHandlerService;
import com.capstone.toolscheduler.kafka.producer.AckScanRequestJobEventProducer;
import com.capstone.toolscheduler.kafka.producer.ScanParseEventProducer;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ScanRequestEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanRequestEventConsumer.class);

    private final TenantRepository tenantRepository;
    private final CodeScanRequestHandlerService codeScanRequestHandlerService;
    private final DependabotScanRequestHandlerService dependabotScanRequestHandlerService;
    private final SecretScanRequestHandlerService secretScanRequestHandlerService;
    private final ObjectMapper objectMapper;

    // Inject new producers for ack & parse job
    private final AckScanRequestJobEventProducer ackProducer;
    private final ScanParseEventProducer parseJobProducer;

    public ScanRequestEventConsumer(
            CodeScanRequestHandlerService codeScanRequestHandlerService,
            DependabotScanRequestHandlerService dependabotScanRequestHandlerService,
            SecretScanRequestHandlerService secretScanRequestHandlerService,
            TenantRepository tenantRepository,
            ObjectMapper objectMapper,
            AckScanRequestJobEventProducer ackProducer,
            ScanParseEventProducer parseJobProducer
    ) {
        this.codeScanRequestHandlerService = codeScanRequestHandlerService;
        this.dependabotScanRequestHandlerService = dependabotScanRequestHandlerService;
        this.secretScanRequestHandlerService = secretScanRequestHandlerService;
        this.tenantRepository = tenantRepository;
        this.objectMapper = objectMapper;
        this.ackProducer = ackProducer;
        this.parseJobProducer = parseJobProducer;
    }

    @KafkaListener(
        topics = "#{T(com.capstone.toolscheduler.model.KafkaTopic).TOOLSCHEDULER_JFC.getTopicName()}",
        groupId = "${spring.kafka.consumer.group-id:toolscheduler-consumer-group}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(String rawJson) {
        Long wholeJobId = null;
        try {
            if(!rawJson.contains("\"SCAN_REQUEST_JOB\"")) {
                return;
            }
            LOGGER.info("Received ScanRequestJobEvent (from JFC) as raw JSON: {}", rawJson);

            ScanRequestJobEvent scanRequestJobEvent =
                objectMapper.readValue(rawJson, ScanRequestJobEvent.class);

            ScanRequestJobEventPayload payload = scanRequestJobEvent.getPayload();
            wholeJobId = payload.getJobId();
            String owner = payload.getOwner();
            String repository = payload.getRepository();
            Tool tool = payload.getTool();
            Long tenantId = payload.getTenantId();

            String personalAccessToken = tenantRepository.findPatByTenantId(tenantId);
            LOGGER.info(scanRequestJobEvent.toString());
            String filePath = "";
            // 1) Handle the job
            switch (tool) {
                case CODE_SCAN:
                    filePath = codeScanRequestHandlerService.handleAndReturnFilePath(owner, repository, personalAccessToken, tenantId);
                    break;
                case DEPENDABOT:
                    filePath = dependabotScanRequestHandlerService.handleAndReturnFilePath(owner, repository, personalAccessToken, tenantId);
                    break;
                case SECRET_SCAN:
                    filePath = secretScanRequestHandlerService.handleAndReturnFilePath(owner, repository, personalAccessToken, tenantId);
                    break;
                default:
                    LOGGER.error("Unknown scan type: " + tool.getValue());
                    return;
            }

            // Suppose the file with raw findings is saved at:
            // e.g. "/tmp/raw_findings_<eventId>.json"
            // String rawResultsFilePath = "/tmp/raw_findings_" + scanRequestJobEvent.getEventId() + ".json";

            // 2) Produce ACK to JFC
            ackProducer.produce(payload.getJobId(), JobStatus.SUCCESS);
            System.out.println("4. TS processes ScanRequestJob, send ACK to JFC. id: " + scanRequestJobEvent.getEventId());
            // 3) Produce a new parse job event to JFC
            parseJobProducer.produce(tool, filePath, tenantId);
            System.out.println("5.2 TS  sends ScanParseJob to JFC. id: " + scanRequestJobEvent.getEventId());


        } catch (Exception e) {
            LOGGER.error("Error deserializing or processing scan request", e);
            if(wholeJobId != null) {
                ackProducer.produce(wholeJobId, JobStatus.FAILURE);
            }
        }
    }
}
