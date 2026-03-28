package com.alibaba.cloud.ai.mcp.client.component;

import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

public class McpAsyncClientWrapper {

    private final McpAsyncClient client;

    private final List<ToolCallback> toolCallbacks;

    public McpAsyncClientWrapper(McpAsyncClient client, List<ToolCallback> toolCallbacks) {
        this.client = client;
        this.toolCallbacks = toolCallbacks;
    }

    public McpAsyncClient getClient() {
        return client;
    }

    public List<ToolCallback> getToolCallbacks() {
        return toolCallbacks;
    }

}