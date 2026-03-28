//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.function.BiPredicate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.support.ToolUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class SyncMcpToolCallbackProvider implements ToolCallbackProvider {
    private final List<McpSyncClient> mcpClients;
    private final BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter;
    private final boolean returnDirect;

    public SyncMcpToolCallbackProvider(BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter, List<McpSyncClient> mcpClients, boolean returnDirect) {
        Assert.notNull(mcpClients, "MCP clients must not be null");
        Assert.notNull(toolFilter, "Tool filter must not be null");
        this.mcpClients = mcpClients;
        this.toolFilter = toolFilter;
        this.returnDirect = returnDirect;
    }

    public SyncMcpToolCallbackProvider(BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter, List<McpSyncClient> mcpClients) {
        this(toolFilter, mcpClients, false);
    }

    public SyncMcpToolCallbackProvider(List<McpSyncClient> mcpClients) {
        this((mcpClient, tool) -> true, mcpClients);
    }

    public SyncMcpToolCallbackProvider(List<McpSyncClient> mcpClients, boolean returnDirect) {
        this((mcpClient, tool) -> true, mcpClients, returnDirect);
    }

    public SyncMcpToolCallbackProvider(BiPredicate<McpSyncClient, McpSchema.Tool> toolFilter, McpSyncClient... mcpClients) {
        this(toolFilter, List.of(mcpClients));
    }

    public SyncMcpToolCallbackProvider(McpSyncClient... mcpClients) {
        this(List.of(mcpClients));
    }

    public ToolCallback[] getToolCallbacks() {
        ToolCallback[] array = (ToolCallback[])this.mcpClients.stream().flatMap((mcpClient) -> mcpClient.listTools().tools().stream().filter((tool) -> this.toolFilter.test(mcpClient, tool)).map((tool) -> new SyncMcpToolCallback(mcpClient, tool, this.returnDirect))).toArray((x$0) -> new ToolCallback[x$0]);
        this.validateToolCallbacks(array);
        return array;
    }

    private void validateToolCallbacks(ToolCallback[] toolCallbacks) {
        List<String> duplicateToolNames = ToolUtils.getDuplicateToolNames(toolCallbacks);
        if (!duplicateToolNames.isEmpty()) {
            throw new IllegalStateException("Multiple tools with the same name (%s)".formatted(String.join(", ", duplicateToolNames)));
        }
    }

    public static List<ToolCallback> syncToolCallbacks(List<McpSyncClient> mcpClients) {
        return CollectionUtils.isEmpty(mcpClients) ? List.of() : List.of((new SyncMcpToolCallbackProvider(mcpClients)).getToolCallbacks());
    }
}
