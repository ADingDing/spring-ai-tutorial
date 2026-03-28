package com.spring.ai.tutorial.mcp.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

/**
 * @author yingzi
 * @date 2025/6/4 18:09
 */
//@SpringBootApplication(scanBasePackages = {"com.spring.ai.tutorial.mcp.client","com.alibaba.cloud.ai"})
@SpringBootApplication
public class Nacos3ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nacos3ClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, @Qualifier("distributedAsyncToolCallback") ToolCallbackProvider tools,
                                                 ConfigurableApplicationContext context) {

        ToolCallback[] toolCallbacks = tools.getToolCallbacks();
        System.out.println(">>> Available tools: ");
        for (int i = 0; i < toolCallbacks.length; i++) {
            System.out.println("[" + i + "] " + toolCallbacks[i].getToolDefinition().name());
        }

        return args -> {
            var chatClient = chatClientBuilder
                    .defaultToolCallbacks(toolCallbacks)
                    .build();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\n>>> QUESTION: ");
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                if (userInput.isEmpty()) {
                    userInput = "北京时间现在几点钟";
                }
                System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());
            }
            scanner.close();
            context.close();
        };
    }
}
