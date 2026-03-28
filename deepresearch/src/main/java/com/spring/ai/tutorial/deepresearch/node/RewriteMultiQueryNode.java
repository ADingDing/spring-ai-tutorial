package com.spring.ai.tutorial.deepresearch.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphEdgeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphNodeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphStateConstant;
import com.spring.ai.tutorial.deepresearch.util.PromptUtil;
import com.spring.ai.tutorial.deepresearch.util.StateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientAttributes;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public class RewriteMultiQueryNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(RewriteMultiQueryNode.class);

    private final ChatClient rewriteMultiQueryAgent;

    private final ListOutputConverter listConverter = new ListOutputConverter(new DefaultConversionService());

    public RewriteMultiQueryNode(ChatClient.Builder chatClientBuilder) {
        this.rewriteMultiQueryAgent = chatClientBuilder
                .defaultSystem(PromptUtil.loadRewriteMultiQueryPrompt())
                .build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        logger.info("rewrite_multi_query node is running.");
        String nextStep = GraphNodeConstant.BACKGROUND_INVESTIGATION.getValue();
        Map<String, Object> updated = new HashMap<>();

        List<String> queryList = rewriteMultiQueryAgent.prompt(StateUtil.getQuery(state))
                .advisors(
                        a -> a.param(ChatClientAttributes.OUTPUT_FORMAT.getKey(), listConverter.getFormat())
                ).call().entity(listConverter);

        updated.put(GraphStateConstant.OPTIMIZE_QUERIES.getValue(), queryList);
        updated.put(GraphEdgeConstant.REWRITE_MULTI_QUERY_EDGE.getValue(), nextStep);
        return updated;
    }
}
