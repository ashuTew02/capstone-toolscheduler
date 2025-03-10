package com.capstone.toolscheduler.service;

import com.capstone.toolscheduler.model.Tool;
import com.capstone.toolscheduler.repository.TenantRepository;
import com.capstone.toolscheduler.utils.ScanStoragePath;
import com.capstone.toolscheduler.utils.StoreJSONContentToFileSystemUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DependabotScanRequestHandlerService implements ScanRequestHandlerService {
    private final WebClient.Builder webClientBuilder;

    public DependabotScanRequestHandlerService(WebClient.Builder webClientBuilder, TenantRepository tenantRepository) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public String handleAndReturnFilePath(String owner, String repository, String personalAccessToken, Long tenantId) throws Exception {
        String tool = Tool.DEPENDABOT.getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> totalAlerts = new ArrayList<>();
        int page = 1;
        int perPage = 100;
        while (true) {
            String url = "https://api.github.com/repos/" + owner + "/" + repository + "/dependabot/alerts?per_page=" + perPage + "&page=" + page;
            String responseData = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .header("Authorization", "Bearer " + personalAccessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            List<Map<String, Object>> alerts = objectMapper.readValue(responseData, new TypeReference<List<Map<String, Object>>>() {});
            if (alerts.isEmpty()) break;
            totalAlerts.addAll(alerts);
            if (alerts.size() < perPage) break;
            page++;
        }
        String finalData = objectMapper.writeValueAsString(totalAlerts);
        String directoryPath = ScanStoragePath.get(tool, tenantId, owner, repository);
        String filePath = StoreJSONContentToFileSystemUtil.storeFile(directoryPath, finalData);
        return filePath;
        // scanJobEventProducer.produce(Tool.DEPENDABOT, filePath, tenantId);
    }
}
