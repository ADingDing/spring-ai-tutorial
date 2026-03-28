package com.alibaba.cloud.ai.mcp.client.component;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public class McpSyncClientWrapper {

    private final McpSyncClient client;

    private final List<ToolCallback> toolCallbacks;

    public McpSyncClientWrapper(McpSyncClient client, List<ToolCallback> toolCallbacks) {
        this.client = client;
        this.toolCallbacks = toolCallbacks;
    }

    public McpSyncClient getClient() {
        return client;
    }

    public List<ToolCallback> getToolCallbacks() {
        return toolCallbacks;
    }

}