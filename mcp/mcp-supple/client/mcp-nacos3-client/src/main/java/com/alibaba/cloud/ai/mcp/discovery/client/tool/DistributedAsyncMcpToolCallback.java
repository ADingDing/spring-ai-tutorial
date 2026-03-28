package com.alibaba.cloud.ai.mcp.discovery.client.tool;

import com.alibaba.cloud.ai.mcp.discovery.client.transport.DistributedAsyncMcpClient;
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

public class DistributedAsyncMcpToolCallback implements ToolCallback {

    private final DistributedAsyncMcpClient distributedAsyncMcpClient;

    private final McpSchema.Tool tool;

    public DistributedAsyncMcpToolCallback(DistributedAsyncMcpClient distributedAsyncMcpClient, McpSchema.Tool tool) {
        Assert.notNull(distributedAsyncMcpClient, "distributedSyncClient must not be null");
        Assert.notNull(tool, "tool must not be null");
        this.distributedAsyncMcpClient = distributedAsyncMcpClient;
        this.tool = tool;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        return ToolDefinition.builder()
                .name(McpToolUtils.prefixedToolName(this.distributedAsyncMcpClient.getServerName(), this.tool.name()))
                .description(this.tool.description())
                .inputSchema(ModelOptionsUtils.toJsonString(this.tool.inputSchema()))
                .build();    }

    @Override
    public String call(String toolInput) {
        Map<String, Object> arguments = ModelOptionsUtils.jsonToMap(toolInput);
        return this.distributedAsyncMcpClient
                .callTool(new McpSchema.CallToolRequest(this.tool.name(), arguments))
                .map((response) -> {
                    if (response.isError() != null && response.isError()) {
                        throw new IllegalStateException("Error calling tool: " + String.valueOf(response.content()));
                    }
                    else {
                        return ModelOptionsUtils.toJsonString(response.content());
                    }
                })
                .block();
    }
}
