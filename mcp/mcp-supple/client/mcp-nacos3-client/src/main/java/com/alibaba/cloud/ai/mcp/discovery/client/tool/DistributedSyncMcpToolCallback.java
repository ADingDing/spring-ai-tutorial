package com.alibaba.cloud.ai.mcp.discovery.client.tool;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedSyncMcpClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public class DistributedSyncMcpToolCallback implements ToolCallback {

    private final DistributedSyncMcpClient distributedSyncMcpClient;

    private final McpSchema.Tool tool;

    public DistributedSyncMcpToolCallback(DistributedSyncMcpClient distributedSyncMcpClient, McpSchema.Tool tool) {
        Assert.notNull(distributedSyncMcpClient, "distributedSyncClient must not be null");
        Assert.notNull(tool, "tool must not be null");
        this.distributedSyncMcpClient = distributedSyncMcpClient;
        this.tool = tool;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition.builder()
                .name(McpToolUtils.prefixedToolName(this.distributedSyncMcpClient.getServerName(), this.tool.name()))
                .description(this.tool.description())
                .inputSchema(ModelOptionsUtils.toJsonString(this.tool.inputSchema()))
                .build();    }

    @Override
    public String call(String toolInput) {
        Map<String, Object> arguments = ModelOptionsUtils.jsonToMap(toolInput);
        McpSchema.CallToolResult response = this.distributedSyncMcpClient
                .callTool(new McpSchema.CallToolRequest(this.tool.name(), arguments));
        if (response.isError() != null && response.isError()) {
            throw new IllegalStateException("Error calling tool: " + response.content());
        }
        else {
            return ModelOptionsUtils.toJsonString(response.content());
        }
    }
}
