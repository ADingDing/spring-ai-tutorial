package com.spring.ai.tutorial.mcp.client.component;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yingzi
 * @since 2025/7/20
 */
@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(CustomMcpSyncClientCustomizer.class);

    @Override
    public void customize(String name, McpClient.SyncSpec spec) {
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            logger.info("tools change");
        });
    }
}
