package com.capstone.toolscheduler.service;

public interface ScanRequestHandlerService {
    String handleAndReturnFilePath(String owner, String repository, String personalAccessToken, Long tenantId) throws Exception;
}
