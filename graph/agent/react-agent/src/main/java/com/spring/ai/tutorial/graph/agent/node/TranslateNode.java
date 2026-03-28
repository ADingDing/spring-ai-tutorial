package com.spring.ai.tutorial.graph.agent.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/6/13
 */

public class TranslateNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(TranslateNode.class);

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("Given a user query, translate it to {targetLanguage}.\nIf the query is already in {targetLanguage}, return it unchanged.\nIf you don't know the language of the query, return it unchanged.\nDo not add explanations nor any other text.\n\nOriginal query: {query}\n\nTranslated query:\n");

    private final ChatClient chatClient;

    private final String  TARGET_LANGUAGE= "English";

    public TranslateNode(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        logger.info("TranslateNode apply start");

        String query = state.value("query", "");
        String targetLanguage = state.value("translate_language", TARGET_LANGUAGE);

        Flux<String> streamResult = this.chatClient.prompt().user((user) -> user.text(DEFAULT_PROMPT_TEMPLATE.getTemplate()).param("targetLanguage", targetLanguage).param("query", query)).stream().content();
        String result = streamResult.reduce("", (acc, item) -> acc + item).block();

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("translate_content", result);
        return resultMap;
    }
}
