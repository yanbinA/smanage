package com.temple.manage.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Random;

@Component
@Slf4j
public class FileUtil {
    public static String baseUrl;
    public static String basePath;

    private FileUtil(
            @Value("${upload.location}") String location,
            @Value("${upload.remote}") String remote) {
        log.info("upload.location: {}", location);
        log.info("upload.remote: {}", remote);
        baseUrl = remote;
        basePath = location;
    }

    public static String getUrl(String path) {
        return path.replace(basePath, baseUrl);
    }

    public static String generalFilename(String filename) {
        StringBuilder sb = new StringBuilder(generalDir());
        int index = filename.lastIndexOf(".");
        String suffix = index == -1 ? "" : filename.substring(index);
        sb.append("file_").append(System.currentTimeMillis()).append(suffix);
        return sb.toString();
    }

    public static String generalDir() {
        StringBuilder sb = new StringBuilder(basePath);
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            sb.append(random.nextInt(16)).append("/");
        }
        String dir = sb.toString();
        mkdir(dir);
        return dir;
    }

    private static void mkdir(String path) {
        File fd = null;
        try {
            fd = new File(path);
            if (!fd.exists()) {
                if (!fd.mkdirs()) {
                    log.error("create dir error {}", path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fd = null;
        }
    }
}
