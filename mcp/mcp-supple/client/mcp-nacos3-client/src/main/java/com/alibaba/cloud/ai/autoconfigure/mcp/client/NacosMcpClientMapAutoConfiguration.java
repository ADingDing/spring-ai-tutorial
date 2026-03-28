package com.alibaba.cloud.ai.autoconfigure.mcp.client;

import com.alibaba.cloud.ai.autoconfigure.mcp.nacos.NacosMcpAutoConfiguration;
import com.alibaba.cloud.ai.mcp.discovery.client.nacos.NacosMcpClientProperties;
import com.alibaba.cloud.ai.mcp.discovery.client.nacos.NacosMcpSseClientProperties;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedAsyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedSyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.sse.SseWebFluxDistributedAsyncMcpClient;
import com.alibaba.cloud.ai.mcp.discovery.client.transport.sse.SseWebFluxDistributedSyncMcpClient;
import com.alibaba.cloud.ai.mcp.nacos.service.NacosMcpOperationService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/10/28
 */
@AutoConfiguration(after = { NacosMcpAutoConfiguration.class })
@EnableConfigurationProperties({ NacosMcpSseClientProperties.class, NacosMcpClientProperties.class})
@ConditionalOnProperty(prefix = "spring.ai.alibaba.mcp.nacos.client", name = { "enabled" }, havingValue = "true",
        matchIfMissing = false)
public class NacosMcpClientMapAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "SYNC",
            matchIfMissing = true)
    public List<DistributedSyncMcpClient> sseWebFluxDistributedSyncClients(
            ObjectProvider<Map<String, NacosMcpOperationService>> nacosMcpOperationServiceMapProvider,
            NacosMcpSseClientProperties nacosMcpSseClientProperties, ApplicationContext applicationContext) {
        Map<String, NacosMcpOperationService> nacosMcpOperationServiceMap = nacosMcpOperationServiceMapProvider.getObject();
        List<DistributedSyncMcpClient> clients = new ArrayList<>();

        nacosMcpSseClientProperties.getConnections().forEach((name, nacosSseParameters) -> {
            SseWebFluxDistributedSyncMcpClient client = SseWebFluxDistributedSyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationServiceMap.get(name))
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();
            clients.add(client);

        });
        return clients;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "ASYNC",
            matchIfMissing = true)
    public List<DistributedAsyncMcpClient> sseWebFluxDistributedAsyncClients(
            ObjectProvider<Map<String, NacosMcpOperationService>> nacosMcpOperationServiceMapProvider,
            NacosMcpSseClientProperties nacosMcpSseClientProperties, ApplicationContext applicationContext) {
        Map<String, NacosMcpOperationService> nacosMcpOperationServiceMap = nacosMcpOperationServiceMapProvider.getObject();
        List<DistributedAsyncMcpClient> clients = new ArrayList<>();

        nacosMcpSseClientProperties.getConnections().forEach((name, nacosSseParameters) -> {
            SseWebFluxDistributedAsyncMcpClient client = SseWebFluxDistributedAsyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationServiceMap.get(name))
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();

            clients.add(client);
        });
        return clients;
    }
}
