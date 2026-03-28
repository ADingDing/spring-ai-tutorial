package org.springframework.ai.tool.method;

import org.springframework.ai.tool.definition.ToolDefinition;

/**
 * @author yingzi
 * @since 2025/7/9
 */

public interface DynamicMcpToolsProvider {

    void addTool(final ToolDefinition toolDefinition);

    void removeTool(final String toolName);

}
