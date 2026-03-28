package com.alibaba.cloud.ai.mcp.discovery.client.nacos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/10/28
 */
@ConfigurationProperties(NacosMcpClientProperties.CONFIG_PREFIX)
public class NacosMcpClientProperties {

    public static final String CONFIG_PREFIX = "spring.ai.alibaba.mcp.nacos.client";

    private final Map<String, NacosConfig> configs = new HashMap<>();

    public Map<String, NacosConfig> getConfigs() {
        return configs;
    }

    public record NacosConfig(String namespace, String serverAddr, String username, String password, String accessKey, String secretKey,
                                     String endpoint) {
    }

}
