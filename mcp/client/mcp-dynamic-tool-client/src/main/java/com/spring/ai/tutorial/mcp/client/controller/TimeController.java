package com.spring.ai.tutorial.mcp.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author yingzi
 * @since 2025/7/20
 */
@RestController
@RequestMapping("/time")
public class TimeController {

    private static final Logger logger = LoggerFactory.getLogger(TimeController.class);

    private final ChatClient chatClient;

    private final SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;

    public TimeController(ChatClient.Builder chatClientBuilder, SyncMcpToolCallbackProvider syncMcpToolCallbackProvider1) {
        this.chatClient = chatClientBuilder.build();
        this.syncMcpToolCallbackProvider = syncMcpToolCallbackProvider1;
    }

    @GetMapping("/chat")
    public String chatTime(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        ToolCallback[] toolCallbacks = syncMcpToolCallbackProvider.getToolCallbacks();
        logger.info("Available tools: {}",
                Arrays.stream(toolCallbacks)
                        .map(cb -> cb.getToolDefinition().name())
                        .collect(Collectors.joining(", ")));

        return chatClient.prompt(query)
                .toolCallbacks(syncMcpToolCallbackProvider.getToolCallbacks())
                .call()
                .content();
    }

}
