package com.alibaba.cloud.ai.autoconfigure.mcp.client;

import com.alibaba.cloud.ai.autoconfigure.mcp.nacos.NacosMcpAutoConfiguration;
import com.alibaba.cloud.ai.mcp.discovery.client.tool.DistributedAsyncMcpToolCallbackProvider;
import com.alibaba.cloud.ai.mcp.discovery.client.tool.DistributedSyncMcpToolCallbackProvider;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedAsyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedSyncMcpClient;
import com.alibaba.cloud.ai.mcp.nacos.NacosMcpProperties;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author yingzi
 * @since 2025/10/25
 */
@AutoConfiguration(after = NacosMcpAutoConfiguration.class)
@EnableConfigurationProperties({ NacosMcpProperties.class })
public class NacosMcpToolCallbackAutoConfiguration {

    @Bean(name = "distributedSyncToolCallback")
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "SYNC",
            matchIfMissing = true)
    public ToolCallbackProvider distributedSyncToolCallback(ObjectProvider<List<DistributedSyncMcpClient>> distributedSyncMcpClients) {
        List<DistributedSyncMcpClient> mcpClients = distributedSyncMcpClients.stream()
                .flatMap(List::stream)
                .toList();
        return new DistributedSyncMcpToolCallbackProvider(mcpClients);
    }

    @Bean(name = "distributedAsyncToolCallback")
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "ASYNC",
    matchIfMissing = false)
    public ToolCallbackProvider distributedAsyncToolCallback(ObjectProvider<List<DistributedAsyncMcpClient>> distributedAsyncMcpClients) {
        List<DistributedAsyncMcpClient> mcpClients = distributedAsyncMcpClients.stream()
                .flatMap(List::stream)
                .toList();
        return new DistributedAsyncMcpToolCallbackProvider(mcpClients);
    }
}
