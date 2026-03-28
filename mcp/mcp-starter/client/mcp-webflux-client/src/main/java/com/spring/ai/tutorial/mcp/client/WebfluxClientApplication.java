package com.spring.ai.tutorial.mcp.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

/**
 * @author yingzi
 * @date 2025/5/28 09:07
 */
@SpringBootApplication
public class WebfluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools,
                                                 ConfigurableApplicationContext context) {

        ToolCallback[] toolCallbacks = tools.getToolCallbacks();
        System.out.println("Available tools:");
        for (ToolCallback toolCallback : toolCallbacks) {
            System.out.println(">>> " + toolCallback.getToolDefinition().name());
        }

        return args -> {
            var chatClient = chatClientBuilder
                    .defaultToolCallbacks(tools.getToolCallbacks())
                    .build();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\n>>> QUESTION: ");
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());
            }
            scanner.close();
            context.close();
        };
    }
}
