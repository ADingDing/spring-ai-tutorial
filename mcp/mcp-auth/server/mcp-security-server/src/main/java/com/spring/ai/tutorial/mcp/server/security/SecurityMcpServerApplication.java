package com.spring.ai.tutorial.mcp.server.security;

import com.spring.ai.tutorial.mcp.server.security.service.TimeService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @since 2025/10/12
 */
@SpringBootApplication
public class SecurityMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityMcpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider timeTools(TimeService timeService) {
        return MethodToolCallbackProvider.builder().toolObjects(timeService).build();
    }
}
