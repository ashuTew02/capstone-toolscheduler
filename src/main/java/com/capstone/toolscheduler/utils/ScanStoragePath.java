package com.capstone.toolscheduler.utils;

import java.nio.file.Paths;

public class ScanStoragePath {
    
    public static String get(String toolString, Long tenantId, String owner, String repository) {
        String projectRoot = System.getProperty("user.dir");
        String directoryPath = Paths.get(projectRoot, "src", "main", "resources", "scans", "tenant-" + tenantId , toolString, owner, repository).toString();
        return directoryPath;
    }
}
