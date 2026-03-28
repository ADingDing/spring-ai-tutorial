//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp.client.autoconfigure;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import java.util.Collection;
import java.util.List;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.ai.mcp.client.common.autoconfigure.McpClientAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@AutoConfiguration(
        after = {McpClientAutoConfiguration.class}
)
@EnableConfigurationProperties({McpClientCommonProperties.class})
@Conditional({McpToolCallbackAutoConfiguration.McpToolCallbackAutoConfigurationCondition.class})
public class McpToolCallbackAutoConfiguration {
    @Bean
    @ConditionalOnProperty(
            prefix = "spring.ai.mcp.client",
            name = {"type"},
            havingValue = "SYNC",
            matchIfMissing = true
    )
    public SyncMcpToolCallbackProvider mcpToolCallbacks(ObjectProvider<List<McpSyncClient>> syncMcpClients, McpClientCommonProperties mcpClientCommonProperties) {
        List<McpSyncClient> mcpClients = syncMcpClients.stream().flatMap(Collection::stream).toList();
        return new SyncMcpToolCallbackProvider(mcpClients, mcpClientCommonProperties.isReturnDirect());
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.ai.mcp.client",
            name = {"type"},
            havingValue = "ASYNC"
    )
    public AsyncMcpToolCallbackProvider mcpAsyncToolCallbacks(ObjectProvider<List<McpAsyncClient>> mcpClientsProvider, McpClientCommonProperties mcpClientCommonProperties) {
        List<McpAsyncClient> mcpClients = mcpClientsProvider.stream().flatMap(Collection::stream).toList();
        return new AsyncMcpToolCallbackProvider(mcpClients, mcpClientCommonProperties.isReturnDirect());
    }

    public static class McpToolCallbackAutoConfigurationCondition extends AllNestedConditions {
        public McpToolCallbackAutoConfigurationCondition() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(
                prefix = "spring.ai.mcp.client",
                name = {"enabled"},
                havingValue = "true",
                matchIfMissing = true
        )
        static class McpAutoConfigEnabled {
        }

        @ConditionalOnProperty(
                prefix = "spring.ai.mcp.client.toolcallback",
                name = {"enabled"},
                havingValue = "true",
                matchIfMissing = true
        )
        static class ToolCallbackProviderEnabled {
        }
    }
}
