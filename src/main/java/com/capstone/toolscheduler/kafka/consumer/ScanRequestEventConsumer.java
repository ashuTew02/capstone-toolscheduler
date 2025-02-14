package com.capstone.toolscheduler.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.dto.event.ScanRequestJobEvent;
import com.capstone.toolscheduler.dto.event.payload.ScanRequestJobEventPayload;
import com.capstone.toolscheduler.model.Tool;
import com.capstone.toolscheduler.repository.TenantRepository;
import com.capstone.toolscheduler.service.CodeScanRequestHandlerService;
import com.capstone.toolscheduler.service.DependabotScanRequestHandlerService;
import com.capstone.toolscheduler.service.SecretScanRequestHandlerService;

@Component
public class ScanRequestEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanRequestEventConsumer.class);

    private final TenantRepository tenantRepository;
    private final CodeScanRequestHandlerService codeScanRequestHandlerService;
    private final DependabotScanRequestHandlerService dependabotScanRequestHandlerService;
    private final SecretScanRequestHandlerService secretScanRequestHandlerService;

    public ScanRequestEventConsumer(CodeScanRequestHandlerService codeScanRequestHandlerService, 
                                    DependabotScanRequestHandlerService dependabotScanRequestHandlerService, 
                                    SecretScanRequestHandlerService secretScanRequestHandlerService,
                                    TenantRepository tenantRepository) {
        this.codeScanRequestHandlerService = codeScanRequestHandlerService;
        this.dependabotScanRequestHandlerService = dependabotScanRequestHandlerService;
        this.secretScanRequestHandlerService = secretScanRequestHandlerService;
        this.tenantRepository = tenantRepository;
    }

    @KafkaListener(topics = "${kafka.topics.scan-request:scan_request}",
            groupId = "${spring.kafka.consumer.group-id:toolscheduler-consumer-group}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ScanRequestJobEvent scanRequestJobEvent) {
        ScanRequestJobEventPayload scanRequestJobEventPayload = scanRequestJobEvent.getPayload();

        String owner = scanRequestJobEventPayload.getOwner();
        String repository = scanRequestJobEventPayload.getRepository();
        Tool tool = scanRequestJobEventPayload.getTool();
        Long tenantId = scanRequestJobEventPayload.getTenantId();
        LOGGER.info("Received scan requests for " + "tenantId " + tenantId + " " + owner + "/" + repository);
        
        String personalAccessToken = tenantRepository.findPatByTenantId(tenantId);

        try {
            switch (tool) {
                case CODE_SCAN:
                    codeScanRequestHandlerService.handle(owner, repository, personalAccessToken, tenantId);
                    break;
                case DEPENDABOT:
                    dependabotScanRequestHandlerService.handle(owner, repository, personalAccessToken, tenantId);
                    break;
                case SECRET_SCAN:
                    secretScanRequestHandlerService.handle(owner, repository, personalAccessToken, tenantId);
                    break;
                default:
                    LOGGER.error("Unknown scan type: " + tool.getValue());
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error processing scan request", e);
        }
    }
}
