package com.spring.ai.tutorial.deepresearch.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphEdgeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphNodeConstant;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphStateConstant;
import com.spring.ai.tutorial.deepresearch.tool.PlannerTool;
import com.spring.ai.tutorial.deepresearch.util.PromptUtil;
import com.spring.ai.tutorial.deepresearch.util.StateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingChatOptions;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @since 2025/5/18 16:38
 */

public class CoordinatorNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorNode.class);

    private final ChatClient coordinatorAgent;

    public CoordinatorNode(ChatClient.Builder chatClientBuilder) {
        this.coordinatorAgent = chatClientBuilder
                .defaultOptions(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(false) // 禁用内部工具执行
                        .build())
                .defaultTools(new PlannerTool()) // 绑定计划节点
                .defaultSystem(PromptUtil.loadCoordinatorPrompt()) // 加载系统提示
                .build()
        ;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        logger.info("coordinator node is running.");
        String nextStep = END;
        Map<String, Object> updated = new HashMap<>();

        ChatResponse chatResponse = coordinatorAgent.prompt(StateUtil.getQuery(state)).call().chatResponse();
        // 获取 assistant 消息内容
        assert chatResponse != null;
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        // 判断是否触发工具调用
        if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
            logger.info("✅ 工具已调用: " + assistantMessage.getToolCalls());
            nextStep = GraphNodeConstant.REWRITE_MULTI_QUERY.getValue();
        }
        else {
            logger.warn("❌ 未触发工具调用");
            logger.debug("Coordinator response: {}", chatResponse.getResult());
            updated.put(GraphStateConstant.OUTPUT.getValue(), assistantMessage.getText());
        }
        updated.put(GraphEdgeConstant.COORDINATOR_EDGE.getValue(), nextStep);

        return updated;
    }
}
