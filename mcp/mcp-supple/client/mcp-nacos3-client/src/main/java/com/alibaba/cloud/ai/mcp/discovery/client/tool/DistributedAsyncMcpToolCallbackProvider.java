package com.alibaba.cloud.ai.mcp.discovery.client.tool;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedAsyncMcpClient;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.support.ToolUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public class DistributedAsyncMcpToolCallbackProvider implements ToolCallbackProvider {

    private final List<DistributedAsyncMcpClient> mcpClients;

    private final BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter;

    public DistributedAsyncMcpToolCallbackProvider(BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter, List<DistributedAsyncMcpClient> mcpClients) {
        Assert.notNull(mcpClients, "mcpClients cannot be null");
        Assert.notNull(toolFilter, "toolFilter cannot be null");
        this.mcpClients = mcpClients;
        this.toolFilter = toolFilter;
    }

    public DistributedAsyncMcpToolCallbackProvider(List<DistributedAsyncMcpClient> mcpClients) {
        this((mcpClient, tool) -> true, mcpClients);
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        List<ToolCallback> toolCallbackList = new ArrayList();
        Iterator var2 = this.mcpClients.iterator();

        while (var2.hasNext()) {
            DistributedAsyncMcpClient mcpClient = (DistributedAsyncMcpClient) var2.next();
            ToolCallback[] toolCallbacks = (ToolCallback[]) mcpClient.listTools().map((response) -> {
                return (ToolCallback[]) response.tools().stream().filter((tool) -> {
                    return this.toolFilter.test(mcpClient.getMcpAsyncClient(), tool);
                }).map((tool) -> {
                    return new DistributedAsyncMcpToolCallback(mcpClient, tool);
                }).toArray((x$0) -> {
                    return new ToolCallback[x$0];
                });
            }).block();
            this.validateToolCallbacks(toolCallbacks);
            toolCallbackList.addAll(List.of(toolCallbacks));
        }

        return (ToolCallback[]) toolCallbackList.toArray(new ToolCallback[0]);
    }

    private void validateToolCallbacks(ToolCallback[] toolCallbacks) {
        List<String> duplicateToolNames = ToolUtils.getDuplicateToolNames(toolCallbacks);
        if (!duplicateToolNames.isEmpty()) {
            throw new IllegalStateException(
                    "Multiple tools with the same name (%s)".formatted(String.join(", ", duplicateToolNames)));
        }
    }
}
