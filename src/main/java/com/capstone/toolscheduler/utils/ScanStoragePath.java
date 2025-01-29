package com.capstone.toolscheduler.utils;

import java.nio.file.Paths;

public class ScanStoragePath {
    
    public static String get(String scanType, String owner, String repository) {
        String projectRoot = System.getProperty("user.dir");
        String directoryPath = Paths.get(projectRoot, "src", "main", "resources", "scans", scanType, owner, repository).toString();
        return directoryPath;
    }
}
