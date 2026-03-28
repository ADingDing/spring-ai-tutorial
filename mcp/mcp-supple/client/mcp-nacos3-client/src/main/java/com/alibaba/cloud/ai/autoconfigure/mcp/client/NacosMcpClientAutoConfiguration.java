package com.alibaba.cloud.ai.autoconfigure.mcp.client;

import com.alibaba.cloud.ai.autoconfigure.mcp.nacos.NacosMcpAutoConfiguration;
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

/**
 * @author yingzi
 * @since 2025/10/25
 */
@AutoConfiguration(after = { NacosMcpAutoConfiguration.class })
@EnableConfigurationProperties({ NacosMcpSseClientProperties.class })
@ConditionalOnProperty(prefix = "spring.ai.alibaba.mcp.nacos.client", name = { "enabled" }, havingValue = "true",
        matchIfMissing = false)
public class NacosMcpClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "SYNC",
            matchIfMissing = true)
    public List<DistributedSyncMcpClient> sseWebFluxDistributedSyncClients(
            ObjectProvider<NacosMcpOperationService> nacosMcpOperationServiceProvider,
            NacosMcpSseClientProperties nacosMcpSseClientProperties, ApplicationContext applicationContext) {
        NacosMcpOperationService nacosMcpOperationService = nacosMcpOperationServiceProvider.getObject();

        List<DistributedSyncMcpClient> clients = new ArrayList<>();

        for (NacosMcpSseClientProperties.NacosSseParameters nacosSseParameters : nacosMcpSseClientProperties.getConnections().values()) {
            SseWebFluxDistributedSyncMcpClient client = SseWebFluxDistributedSyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationService)
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();

            clients.add(client);
        }
        return clients;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = { "type" }, havingValue = "ASYNC",
            matchIfMissing = true)
    public List<DistributedAsyncMcpClient> sseWebFluxDistributedAsyncClients(
            ObjectProvider<NacosMcpOperationService> nacosMcpOperationServiceProvider,
            NacosMcpSseClientProperties nacosMcpSseClientProperties, ApplicationContext applicationContext) {
        NacosMcpOperationService nacosMcpOperationService = nacosMcpOperationServiceProvider.getObject();

        List<DistributedAsyncMcpClient> clients = new ArrayList<>();

        for (NacosMcpSseClientProperties.NacosSseParameters nacosSseParameters : nacosMcpSseClientProperties.getConnections().values()) {
            SseWebFluxDistributedAsyncMcpClient client = SseWebFluxDistributedAsyncMcpClient.builder()
                    .serverName(nacosSseParameters.serviceName())
                    .version(nacosSseParameters.version())
                    .nacosMcpOperationService(nacosMcpOperationService)
                    .applicationContext(applicationContext)
                    .build();
            client.init();
            client.subscribe();

            clients.add(client);
        }
        return clients;
    }
}
