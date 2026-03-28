//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.execution.ToolExecutionException;
import org.springframework.ai.tool.metadata.DefaultToolMetadata;
import org.springframework.ai.tool.metadata.ToolMetadata;

public class SyncMcpToolCallback implements ToolCallback {
    private static final Logger logger = LoggerFactory.getLogger(SyncMcpToolCallback.class);
    private final McpSyncClient mcpClient;
    private final McpSchema.Tool tool;
    private final boolean returnDirect;

    public SyncMcpToolCallback(McpSyncClient mcpClient, McpSchema.Tool tool) {
        this(mcpClient, tool, false);
    }

    public SyncMcpToolCallback(McpSyncClient mcpClient, McpSchema.Tool tool, boolean returnDirect) {
        this.mcpClient = mcpClient;
        this.tool = tool;
        this.returnDirect = returnDirect;
    }

    public ToolMetadata getToolMetadata() {
        return DefaultToolMetadata.builder()
                .returnDirect(returnDirect)
                .build();
    }

    public ToolDefinition getToolDefinition() {
        return DefaultToolDefinition.builder().name(McpToolUtils.prefixedToolName(this.mcpClient.getClientInfo().name(), this.tool.name())).description(this.tool.description()).inputSchema(ModelOptionsUtils.toJsonString(this.tool.inputSchema())).build();
    }

    public String call(String functionInput) {
        Map<String, Object> arguments = ModelOptionsUtils.jsonToMap(functionInput);

        McpSchema.CallToolResult response;
        try {
            response = this.mcpClient.callTool(new McpSchema.CallToolRequest(this.tool.name(), arguments));
        } catch (Exception ex) {
            logger.error("Exception while tool calling: ", ex);
            throw new ToolExecutionException(this.getToolDefinition(), ex);
        }

        if (response.isError() != null && response.isError()) {
            logger.error("Error calling tool: {}", response.content());
            throw new ToolExecutionException(this.getToolDefinition(), new IllegalStateException("Error calling tool: " + String.valueOf(response.content())));
        } else {
            return ModelOptionsUtils.toJsonString(response.content());
        }
    }

    public String call(String toolArguments, ToolContext toolContext) {
        return this.call(toolArguments);
    }
}
