package com.capstone.toolscheduler.kafka.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.capstone.toolscheduler.dto.event.ScanRequestEvent;
import com.capstone.toolscheduler.model.ScanType;
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
    public void onMessage(ScanRequestEvent scanRequestEvent) {
        String owner = scanRequestEvent.getOwner();
        String repository = scanRequestEvent.getRepository();
        List<ScanType> scanTypes = scanRequestEvent.getScanTypes();
        Long tenantId = scanRequestEvent.getTenantId();
        LOGGER.info("Received scan requests for " + owner + "/" + repository);
        
        String personalAccessToken = tenantRepository.findPatByTenantId(tenantId);
        String findingsEsIndex = tenantRepository.findEsIndexByTenantId(tenantId);

        try {
            if(scanTypes.contains(ScanType.ALL)) {
                codeScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
                dependabotScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
                secretScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
            } else {
                for(ScanType scanType : scanTypes) {
                    String scanTypeVal = scanType.getValue();
                    // Boolean isUnknown = false;
                    switch (scanTypeVal) {
                        case "code-scan":
                            codeScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
                            break;
                        case "dependabot":
                            dependabotScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
                            break;
                        case "secret-scan":
                            secretScanRequestHandlerService.handle(owner, repository, personalAccessToken, findingsEsIndex);
                            break;
                        default:
                            LOGGER.error("Unknown scan type: " + scanTypeVal);
                            // isUnknown = true;
                            break;
                    }
                    // if(!isUnknown) {
                    //     LOGGER.info("   Successfully processed " + scanTypeVal + " request for " + owner + "/" + repository);
                    // }
                }
                // LOGGER.info("Successfully processed received scan requests for " + owner + "/" + repository);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing scan request", e);
        }
    }
}
