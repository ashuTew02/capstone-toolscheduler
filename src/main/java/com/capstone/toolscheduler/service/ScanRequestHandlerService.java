package com.capstone.toolscheduler.service;

public interface ScanRequestHandlerService {
    void handle(String owner, String repository, String personalAccessToken, String findingsEsIndex) throws Exception;
}
