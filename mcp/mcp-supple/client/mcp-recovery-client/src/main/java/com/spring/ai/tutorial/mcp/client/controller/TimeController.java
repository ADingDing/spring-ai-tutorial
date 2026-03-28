package com.spring.ai.tutorial.mcp.client.controller;

import com.alibaba.cloud.ai.mcp.client.McpAsyncRecovery;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @since 2025/7/15
 */
@RestController
@RequestMapping("/time")
public class TimeController {

    private final ChatClient chatClient;

    private final McpAsyncRecovery mcpAsyncRecovery;

    public TimeController(ChatClient.Builder chatClientBuilder, McpAsyncRecovery mcpAsyncRecovery) {
        this.chatClient = chatClientBuilder.build();
        this.mcpAsyncRecovery = mcpAsyncRecovery;
    }

    @GetMapping("/chat")
    public String chatTime(@RequestParam(value = "query", defaultValue = "请告诉我现在北京时间几点了") String query) {
        return chatClient.prompt(query)
                .toolCallbacks(mcpAsyncRecovery.getToolCallback())
                .call()
                .content();
    }
}