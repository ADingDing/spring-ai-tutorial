package org.springframework.ai.mcp;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

/**
 * @author yingzi
 * @since 2025/9/21
 */
@Component
public class McpToolCallbackUtil {

    private final ToolCallback[] tools;

    public McpToolCallbackUtil(ToolCallbackProvider tools) {
        this.tools = tools.getToolCallbacks();
    }

    public ToolCallback getToolCallbacByName(String name) {
        for (ToolCallback tool : tools) {
            if (tool.getToolDefinition().name().equals(name)) {
                return tool;
            }
        }
        return null;
    }
}
