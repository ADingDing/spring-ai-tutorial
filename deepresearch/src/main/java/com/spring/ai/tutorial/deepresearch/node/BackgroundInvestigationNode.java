package com.spring.ai.tutorial.deepresearch.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public class BackgroundInvestigationNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(BackgroundInvestigationNode.class);

    private final ChatClient backgroundAgent;

    public BackgroundInvestigationNode(ChatClient.Builder chatClientBuildert) {
        this.backgroundAgent = chatClientBuildert.build();
    }


    @Override
    public Map<String, Object> apply(OverAllState state) {
        logger.info("background investigation node is running.");
        String nextStep = END;
        Map<String, Object> updated = new HashMap<>();

        return Map.of();
    }
}
