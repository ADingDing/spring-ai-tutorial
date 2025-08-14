//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.support.ToolUtils;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

public class AsyncMcpToolCallbackProvider implements ToolCallbackProvider {
    private final List<McpAsyncClient> mcpClients;
    private final BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter;
    private final boolean returnDirect;

    public AsyncMcpToolCallbackProvider(BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter, List<McpAsyncClient> mcpClients, boolean returnDirect) {
        Assert.notNull(mcpClients, "MCP clients must not be null");
        Assert.notNull(toolFilter, "Tool filter must not be null");
        this.mcpClients = mcpClients;
        this.toolFilter = toolFilter;
        this.returnDirect = returnDirect;
    }

    public AsyncMcpToolCallbackProvider(BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter, List<McpAsyncClient> mcpClients) {
        this(toolFilter, mcpClients, false);
    }

    public AsyncMcpToolCallbackProvider(List<McpAsyncClient> mcpClients) {
        this((mcpClient, tool) -> true, mcpClients);
    }

    public AsyncMcpToolCallbackProvider(List<McpAsyncClient> mcpClients, boolean returnDirect) {
        this((mcpClient, tool) -> true, mcpClients, returnDirect);
    }

    public AsyncMcpToolCallbackProvider(BiPredicate<McpAsyncClient, McpSchema.Tool> toolFilter, McpAsyncClient... mcpClients) {
        this(toolFilter, List.of(mcpClients));
    }

    public AsyncMcpToolCallbackProvider(McpAsyncClient... mcpClients) {
        this(List.of(mcpClients));
    }

    public ToolCallback[] getToolCallbacks() {
        List<ToolCallback> toolCallbackList = new ArrayList();

        for(McpAsyncClient mcpClient : this.mcpClients) {
            ToolCallback[] toolCallbacks = (ToolCallback[])mcpClient.listTools().map((response) -> (ToolCallback[])response.tools().stream().filter((tool) -> this.toolFilter.test(mcpClient, tool)).map((tool) -> new AsyncMcpToolCallback(mcpClient, tool, returnDirect)).toArray((x$0) -> new ToolCallback[x$0])).block();
            this.validateToolCallbacks(toolCallbacks);
            toolCallbackList.addAll(List.of(toolCallbacks));
        }

        return (ToolCallback[])toolCallbackList.toArray(new ToolCallback[0]);
    }

    private void validateToolCallbacks(ToolCallback[] toolCallbacks) {
        List<String> duplicateToolNames = ToolUtils.getDuplicateToolNames(toolCallbacks);
        if (!duplicateToolNames.isEmpty()) {
            throw new IllegalStateException("Multiple tools with the same name (%s)".formatted(String.join(", ", duplicateToolNames)));
        }
    }

    public static Flux<ToolCallback> asyncToolCallbacks(List<McpAsyncClient> mcpClients) {
        return CollectionUtils.isEmpty(mcpClients) ? Flux.empty() : Flux.fromArray((new AsyncMcpToolCallbackProvider(mcpClients)).getToolCallbacks());
    }
}
