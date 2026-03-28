package com.spring.ai.tutorial.graph.agent.config;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.agent.ReflectAgent;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.spring.ai.tutorial.graph.agent.node.AssistantGraphNode;
import com.spring.ai.tutorial.graph.agent.node.JudgeGraphNode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @since 2025/8/24
 */
@Configuration
public class ReflectionConfiguration {

    @Bean
    public CompiledGraph reflectionGraph(ChatClient.Builder chatClientBuilder) throws GraphStateException {
        ChatClient chatClient = chatClientBuilder.build();

        AssistantGraphNode assistantGraphNode = AssistantGraphNode.builder().chatClient(chatClient).build();
        JudgeGraphNode judgeGraphNode = JudgeGraphNode.builder().chatClient(chatClient).build();

        ReflectAgent reflectAgent = ReflectAgent.builder()
                .graph(assistantGraphNode)
                .reflection(judgeGraphNode)
                .maxIterations(2)
                .build();

        return reflectAgent.getAndCompileGraph();
    }
}
