package com.spring.ai.tutorial.graph.agent.config;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.spring.ai.tutorial.graph.agent.node.ExpanderNode;
import com.spring.ai.tutorial.graph.agent.node.TranslateNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @since 2025/8/24
 */
@Configuration
public class ReactConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ReactConfiguration.class);

    @Bean
    public ReactAgent normalReactAgent(ChatClient.Builder chatClientBuilder, ToolCallbackResolver resolver) throws GraphStateException {
        ChatClient chatClient = chatClientBuilder
                .defaultToolNames("getCityTimeFunction")
                .build();

        return ReactAgent.builder()
                .name("React Agent Demo")
                .chatClient(chatClient)
                .resolver(resolver)
//                .preLlmHook(new TranslateNode(chatClientBuilder))
//                .postLlmHook(new ExpanderNode(chatClientBuilder))
                .maxIterations(10)
                .build();
    }

    @Bean
    public CompiledGraph reactAgentGraph(@Qualifier("normalReactAgent") ReactAgent reactAgent)
            throws GraphStateException {

        GraphRepresentation graphRepresentation = reactAgent.getStateGraph()
                .getGraph(GraphRepresentation.Type.PLANTUML);

        logger.info("\n\n");
        logger.info(graphRepresentation.content());
        logger.info("\n\n");

        return reactAgent.getAndCompileGraph();
    }
}
