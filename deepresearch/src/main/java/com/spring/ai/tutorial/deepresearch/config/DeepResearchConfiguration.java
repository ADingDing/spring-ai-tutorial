package com.spring.ai.tutorial.deepresearch.config;

import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.KeyStrategyFactoryBuilder;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.spring.ai.tutorial.deepresearch.dispatcher.CoordinatorDispatcher;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphEdgeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphNodeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphStateConstant;
import com.spring.ai.tutorial.deepresearch.node.CoordinatorNode;
import com.spring.ai.tutorial.deepresearch.node.RewriteMultiQueryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * @author yingzi
 * @since 2025/10/1
 */
@Configuration
public class DeepResearchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DeepResearchConfiguration.class);

    @Bean
    public StateGraph deepResearchGraph(ChatClient.Builder chatClientBuilder) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = new KeyStrategyFactoryBuilder()
                .addPatternStrategy(GraphStateConstant.QUERY.getValue(), new ReplaceStrategy())
                .build();

        StateGraph stateGraph = new StateGraph("deep research", keyStrategyFactory)
                .addNode(GraphNodeConstant.COORDINATOR.getValue(), node_async(new CoordinatorNode(chatClientBuilder)))
                .addNode(GraphNodeConstant.REWRITE_MULTI_QUERY.getValue(), node_async(new RewriteMultiQueryNode(chatClientBuilder)))
                .addNode(GraphNodeConstant.BACKGROUND_INVESTIGATION.getValue(), node_async(new RewriteMultiQueryNode(chatClientBuilder)))

                .addEdge(START, GraphNodeConstant.COORDINATOR.getValue())
                .addConditionalEdges(GraphNodeConstant.COORDINATOR.getValue(), edge_async(new CoordinatorDispatcher()),
                        GraphEdgeConstant.COORDINATOR_EDGE.getMappings())
                .addEdge(GraphNodeConstant.REWRITE_MULTI_QUERY.getValue(), GraphNodeConstant.BACKGROUND_INVESTIGATION.getValue())

                ;

        GraphRepresentation graphRepresentation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "workflow graph");
        logger.info("\n\n");
        logger.info(graphRepresentation.content());
        logger.info("\n\n");

        return stateGraph;
    }

}
