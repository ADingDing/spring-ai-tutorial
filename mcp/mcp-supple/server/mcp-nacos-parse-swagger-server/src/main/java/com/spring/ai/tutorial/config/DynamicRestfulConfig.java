package com.spring.ai.tutorial.config;

import com.alibaba.nacos.api.naming.NamingService;
import com.spring.ai.tutorial.component.DynamicRestfulToolsWatch;
import io.modelcontextprotocol.server.McpSyncServer;
import org.springframework.ai.tool.method.DynamicMcpSyncToolsProvider;
import org.springframework.ai.tool.method.DynamicMcpToolsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yingzi
 * @since 2025/7/9
 */
@Configuration
public class DynamicRestfulConfig {

    @Bean
    public DynamicRestfulToolsWatch dynamicRestfulToolsWatch(NamingService namingService, DynamicMcpToolsProvider dynamicMcpToolsProvider, WebClient webClient) {
        DynamicRestfulToolsWatch dynamicRestfulToolsWatch = new DynamicRestfulToolsWatch(namingService, dynamicMcpToolsProvider, webClient);

        dynamicRestfulToolsWatch.subscribe("mcp-nacos-parse-swagger-server");
        return dynamicRestfulToolsWatch;
    }

    @Bean
    public DynamicMcpSyncToolsProvider dynamicMcpSyncToolsProvider(McpSyncServer mcpSyncServer) {
        return new DynamicMcpSyncToolsProvider(mcpSyncServer);
    }
}
