package com.capstone.toolscheduler.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class StoreJSONContentToFileSystemUtil {
    public static String storeFile(String path, String content) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + ".json";
        File file = new File(directory, fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
