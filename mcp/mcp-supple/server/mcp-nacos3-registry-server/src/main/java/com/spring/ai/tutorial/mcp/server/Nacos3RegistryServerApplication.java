package com.spring.ai.tutorial.mcp.server;

import com.spring.ai.tutorial.mcp.server.tool.OpenMeteoService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @since 2025/10/30
 */
@SpringBootApplication
public class Nacos3RegistryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nacos3RegistryServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider timeTools(OpenMeteoService openMeteoService) {
        return MethodToolCallbackProvider.builder().toolObjects(openMeteoService).build();
    }
}
