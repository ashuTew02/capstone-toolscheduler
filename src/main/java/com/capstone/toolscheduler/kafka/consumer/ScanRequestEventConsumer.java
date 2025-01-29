package com.capstone.toolscheduler.kafka.consumer;

import java.util.List;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;

import com.capstone.toolscheduler.dto.event.ScanRequestEvent;
import com.capstone.toolscheduler.model.Credential;
import com.capstone.toolscheduler.model.Credential.CredentialId;
import com.capstone.toolscheduler.model.ScanType;
import com.capstone.toolscheduler.repository.CredentialRepository;
import com.capstone.toolscheduler.service.CodeScanRequestHandlerService;
import com.capstone.toolscheduler.service.DependabotScanRequestHandlerService;
import com.capstone.toolscheduler.service.SecretScanRequestHandlerService;

public class ScanRequestEventConsumer {
    private final CredentialRepository credentialRepository;

    private final CodeScanRequestHandlerService codeScanRequestHandlerService;
    private final DependabotScanRequestHandlerService dependabotScanRequestHandlerService;
    private final SecretScanRequestHandlerService secretScanRequestHandlerService;


    public ScanRequestEventConsumer(CodeScanRequestHandlerService codeScanRequestHandlerService, 
                                        DependabotScanRequestHandlerService dependabotScanRequestHandlerService, 
                                        SecretScanRequestHandlerService secretScanRequestHandlerService,
                                        CredentialRepository credentialRepository) 
    {
        this.codeScanRequestHandlerService = codeScanRequestHandlerService;
        this.dependabotScanRequestHandlerService = dependabotScanRequestHandlerService;
        this.secretScanRequestHandlerService = secretScanRequestHandlerService;
        this.credentialRepository = credentialRepository;
    }

    @KafkaListener(topics = "${kafka.topics.scan-request:scan_request}",
            groupId = "${spring.kafka.consumer.group-id:toolscheduler-consumer-group}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ScanRequestEvent scanRequestEvent) {
        String owner = scanRequestEvent.getOwner();
        String repository = scanRequestEvent.getRepository();
        List<ScanType> scanTypes = scanRequestEvent.getScanTypes();

        // find credential
        CredentialId credentialId = new CredentialId(owner, repository);
        Optional<Credential> credOpt = credentialRepository.findById(credentialId);

        if (credOpt.isEmpty()) {
            System.out.println("Credential not found for " + owner + "/" + repository);
            return;
        }
        String personalAccessToken = credOpt.get().getPersonalAccessToken();

        try {
            if(scanTypes.contains(ScanType.ALL)) {
                codeScanRequestHandlerService.handle(owner, repository, personalAccessToken);
                dependabotScanRequestHandlerService.handle(owner, repository, personalAccessToken);
                secretScanRequestHandlerService.handle(owner, repository, personalAccessToken);
            } else {
                for(ScanType scanType : scanTypes) {
                    String scanTypeVal = scanType.getValue();
    
                    switch (scanTypeVal) {
                        case "code-scan":
                            codeScanRequestHandlerService.handle(owner, repository, personalAccessToken);
                            break;
                        case "dependabot":
                            dependabotScanRequestHandlerService.handle(owner, repository, personalAccessToken);
                            break;
                        case "secret-scan":
                            secretScanRequestHandlerService.handle(owner, repository, personalAccessToken);
                            break;
                        default:
                            // Log unknown type or handle error
                            System.err.println("Unknown scan type: " + scanTypeVal);
                            break;
    
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
