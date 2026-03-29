package com.se2.htmlcsslearning.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceReaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceReaderUtil.class);

    public static String readFile(String type, String challengeName, String fileName) {
        String path = String.format("challenge/%s/%s/%s", type, challengeName, fileName);
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String errorMsg = String.format("Tài nguyên không tìm thấy tại đường dẫn: %s", path);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
}
