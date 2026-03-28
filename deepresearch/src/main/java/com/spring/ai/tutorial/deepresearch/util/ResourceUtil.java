package com.spring.ai.tutorial.deepresearch.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public class ResourceUtil {

    public static String loadPromptByFilePath(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("加载提示词文件失败: " + filePath, e);
        }
    }
}
