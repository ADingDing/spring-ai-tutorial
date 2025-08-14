//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.Map;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.execution.ToolExecutionException;
import org.springframework.ai.tool.metadata.DefaultToolMetadata;
import org.springframework.ai.tool.metadata.ToolMetadata;

public class AsyncMcpToolCallback implements ToolCallback {
    private final McpAsyncClient asyncMcpClient;
    private final McpSchema.Tool tool;
    private final boolean returnDirect;

    public AsyncMcpToolCallback(McpAsyncClient mcpClient, McpSchema.Tool tool) {
        this(mcpClient, tool, false);
    }

    public AsyncMcpToolCallback(McpAsyncClient mcpClient, McpSchema.Tool tool, boolean returnDirect) {
        this.asyncMcpClient = mcpClient;
        this.tool = tool;
        this.returnDirect = returnDirect;
    }

    public ToolMetadata getToolMetadata() {
        return DefaultToolMetadata.builder()
                .returnDirect(returnDirect)
                .build();
    }

    public ToolDefinition getToolDefinition() {
        return DefaultToolDefinition.builder().name(McpToolUtils.prefixedToolName(this.asyncMcpClient.getClientInfo().name(), this.tool.name())).description(this.tool.description()).inputSchema(ModelOptionsUtils.toJsonString(this.tool.inputSchema())).build();
    }

    public String call(String functionInput) {
        Map<String, Object> arguments = ModelOptionsUtils.jsonToMap(functionInput);
        return (String)this.asyncMcpClient.callTool(new McpSchema.CallToolRequest(this.tool.name(), arguments)).onErrorMap((exception) -> {
            throw new ToolExecutionException(this.getToolDefinition(), exception);
        }).map((response) -> {
            if (response.isError() != null && response.isError()) {
                throw new ToolExecutionException(this.getToolDefinition(), new IllegalStateException("Error calling tool: " + String.valueOf(response.content())));
            } else {
                return ModelOptionsUtils.toJsonString(response.content());
            }
        }).block();
    }

    public String call(String toolArguments, ToolContext toolContext) {
        return this.call(toolArguments);
    }
}
