package com.spring.ai.tutorial.mcp.server.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;


/**
 * @author yingzi
 * @since 2025/10/3
 */

@Component
public class CalculatorTools {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorTools.class);

    @McpTool(name = "add", description = "Add two numbers together")
    public int add(
            @McpToolParam(description = "First number", required = true) int a,
            @McpToolParam(description = "Second number", required = true) int b) {
        logger.info("Adding {} and {}", a, b);
        return a + b;
    }

    @McpTool(name = "multiply", description = "Multiply two numbers")
    public double multiply(
            @McpToolParam(description = "First number", required = true) double x,
            @McpToolParam(description = "Second number", required = true) double y) {
        logger.info("Multiplying {} and {}", x, y);
        return x * y;
    }

}
