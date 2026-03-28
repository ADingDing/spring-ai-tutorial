package com.alibaba.cloud.ai.mcp.discovery.client.tool;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedSyncMcpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.support.ToolUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public class DistributedSyncMcpToolCallbackProvider implements ToolCallbackProvider {

    private final List<DistributedSyncMcpClient> mcpClients;

    private final BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter;

    public DistributedSyncMcpToolCallbackProvider(BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter, List<DistributedSyncMcpClient> mcpClients) {
        Assert.notNull(mcpClients, "mcpClients cannot be null");
        Assert.notNull(toolFilter, "toolFilter cannot be null");
        this.mcpClients = mcpClients;
        this.toolFilter = toolFilter;
    }

    public DistributedSyncMcpToolCallbackProvider(List<DistributedSyncMcpClient> mcpClients) {
        this((mcpClient, tool) -> true, mcpClients);
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        ArrayList<Object> toolCallbacks = new ArrayList();

        this.mcpClients.stream().forEach(
                mcpClint -> {
                    toolCallbacks.addAll(mcpClint.listTools().tools().stream().filter((tool) -> {
                        return this.toolFilter.test(mcpClint.getMcpSyncClient(), tool);
                    }).map((tool) -> {
                        return new DistributedSyncMcpToolCallback(mcpClint, tool);
                    }).toList());                }
        );
        ToolCallback[] array = (ToolCallback[]) toolCallbacks.toArray(new ToolCallback[0]);
        this.validateToolCallbacks(array);
        return array;
    }

    private void validateToolCallbacks(ToolCallback[] toolCallbacks) {
        List<String> duplicateToolNames = ToolUtils.getDuplicateToolNames(toolCallbacks);
        if (!duplicateToolNames.isEmpty()) {
            throw new IllegalStateException(
                    "Multiple tools with the same name (%s)".formatted(String.join(", ", duplicateToolNames)));
        }
    }
}
