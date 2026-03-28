package com.alibaba.cloud.ai.mcp.discovery.client.transport;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public interface DistributedSyncMcpClient {

    String getServerName();

    McpSchema.CallToolResult callTool(McpSchema.CallToolRequest callToolRequest);

    McpSchema.ListToolsResult listTools();

    McpSyncClient getMcpSyncClient();
}
