package com.spring.ai.tutorial.graph.node;

import com.alibaba.cloud.ai.graph.GraphResponse;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.streaming.FluxConverter;
import com.alibaba.cloud.ai.graph.streaming.StreamingChatGenerator;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.spring.ai.tutorial.graph.model.NodeStatus;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/6/13
 */

public class ExpanderNode implements NodeAction {

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("You are an expert at information retrieval and search optimization.\nYour task is to generate {number} different versions of the given query.\n\nEach variant must cover different perspectives or aspects of the topic,\nwhile maintaining the core intent of the original query. The goal is to\nexpand the search space and improve the chances of finding relevant information.\n\nDo not explain your choices or add any other text.\nProvide the query variants separated by newlines.\n\nOriginal query: {query}\n\nQuery variants:\n");

    private final ChatClient chatClient;

    private final Integer NUMBER = 3;

    private final Map<String, NodeStatus> node2Status;

    public static final String NODE_NAME = "expander";

    public ExpanderNode(ChatClient.Builder chatClientBuilder, Map<String, NodeStatus> node2Status) {
        this.chatClient = chatClientBuilder.build();
        this.node2Status = node2Status;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        node2Status.put(NODE_NAME, NodeStatus.RUNNING);

        String query = state.value("query", "");
        Integer expanderNumber = state.value("expander_number", this.NUMBER);

        Flux<ChatResponse> chatResponseFlux = this.chatClient.prompt().user((user) -> user.text(DEFAULT_PROMPT_TEMPLATE.getTemplate()).param("number", expanderNumber).param("query", query)).stream().chatResponse();

        Flux<GraphResponse<StreamingOutput>> generator = FluxConverter.builder()
                .startingNode("expander_llm_stream")
                .startingState(state)
                .mapResult(response -> {
                    String text = response.getResult().getOutput().getText();
                    List<String> queryVariants = Arrays.asList(text.split("\n"));
                    node2Status.put(NODE_NAME, NodeStatus.COMPLETED);
                    return Map.of("expander_content", queryVariants);
                }).build(chatResponseFlux);
        return Map.of("expander_content", generator);
    }
}
