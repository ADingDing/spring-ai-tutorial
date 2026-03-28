package com.alibaba.cloud.ai.mcp.discovery.client.transport;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public interface DistributedAsyncMcpClient {

    String getServerName();

    Mono<McpSchema.CallToolResult> callTool(McpSchema.CallToolRequest callToolRequest);

    Mono<McpSchema.ListToolsResult> listTools();

    McpAsyncClient getMcpAsyncClient();
}
