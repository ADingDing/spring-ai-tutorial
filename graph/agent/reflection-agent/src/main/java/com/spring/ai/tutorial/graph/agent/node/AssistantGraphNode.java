package com.spring.ai.tutorial.graph.agent.node;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.agent.ReflectAgent;
import com.alibaba.cloud.ai.graph.node.LlmNode;
import com.alibaba.cloud.ai.graph.state.strategy.AppendStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/8/24
 */

public class AssistantGraphNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(AssistantGraphNode.class);

    private final LlmNode llmNode;

    private SystemPromptTemplate systemPromptTemplate;

    private final String NODE_ID = "assistant_node";

    private static final String CLASSIFIER_PROMPT_TEMPLATE = """
					You are an essay assistant tasked with writing excellent 5-paragraph essays.
				    Generate the best essay possible for the user's request.
				    If the user provides critique, respond with a revised version of your previous attempts.
				    Only return the main content I need, without adding any other interactive language.
				    Please answer in Chinese:
				""";

    public AssistantGraphNode(ChatClient chatClient) {
        this.systemPromptTemplate = new SystemPromptTemplate(CLASSIFIER_PROMPT_TEMPLATE);
        this.llmNode = LlmNode.builder()
                .systemPromptTemplate(systemPromptTemplate.render())
                .chatClient(chatClient)
                .messagesKey("messages")
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map<String, Object> apply(OverAllState overAllState) throws Exception {
        logger.info("assistant_node is running.");

        List<Message> messages = (List<Message>) overAllState.value(ReflectAgent.MESSAGES).get();

        KeyStrategyFactory keyStrategyFactory = () -> {
            HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();

            keyStrategyHashMap.put(ReflectAgent.MESSAGES, new AppendStrategy());
            return keyStrategyHashMap;
        };

        StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                .addNode(this.NODE_ID, AsyncNodeAction.node_async(llmNode))
                .addEdge(StateGraph.START, this.NODE_ID)
                .addEdge(this.NODE_ID, StateGraph.END);

        OverAllState invokeState = stateGraph.compile().call(Map.of(ReflectAgent.MESSAGES, messages)).get();
        List<Message> reactMessages = (List<Message>) invokeState.value(ReflectAgent.MESSAGES).orElseThrow();

        return Map.of(ReflectAgent.MESSAGES, reactMessages);

    }

    public static class Builder {

        private ChatClient chatClient;

        public Builder chatClient(ChatClient chatClient) {
            this.chatClient = chatClient;
            return this;
        }

        public AssistantGraphNode build() {
            if (chatClient == null) {
                throw new IllegalArgumentException("ChatClient must be provided");
            }
            return new AssistantGraphNode(chatClient);
        }

    }

}
