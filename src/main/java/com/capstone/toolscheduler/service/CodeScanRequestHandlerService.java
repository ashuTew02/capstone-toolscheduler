package com.capstone.toolscheduler.service;

import org.springframework.web.reactive.function.client.WebClient;

import com.capstone.toolscheduler.kafka.producer.ScanJobEventProducer;
import com.capstone.toolscheduler.model.ScanType;
import com.capstone.toolscheduler.utils.ScanStoragePath;
import com.capstone.toolscheduler.utils.StoreJSONContentToFileSystemUtil;

public class CodeScanRequestHandlerService implements ScanRequestHandlerService {
    private final WebClient.Builder webClientBuilder;
    private final ScanJobEventProducer scanJobEventProducer;

    public CodeScanRequestHandlerService(WebClient.Builder webClientBuilder, ScanJobEventProducer scanJobEventProducer) {
        this.webClientBuilder = webClientBuilder;
        this.scanJobEventProducer = scanJobEventProducer;
    }
    
    
    @Override
    public void handle(String owner, String repository, String personalAccessToken) throws Exception {
        String type = ScanType.CODE_SCAN.getValue();
        String url = "https://api.github.com/repos/" + owner + "/" + repository + "/code-scanning/alerts";
        String responseData = webClientBuilder.build()
                .get()
                .uri(url)
                .header("Authorization", "Bearer " + personalAccessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        String directoryPath = ScanStoragePath.get(type, owner, repository);
        String filePath = StoreJSONContentToFileSystemUtil.storeFile(directoryPath, responseData);

        scanJobEventProducer.produce(ScanType.CODE_SCAN, filePath);
    }


}
