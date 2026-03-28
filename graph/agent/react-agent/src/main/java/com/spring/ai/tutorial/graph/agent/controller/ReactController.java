package com.spring.ai.tutorial.graph.agent.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yingzi
 * @since 2025/8/24
 */
@RestController
@RequestMapping("/react")
public class ReactController {

    private final CompiledGraph compiledGraph;

    ReactController(@Qualifier("reactAgentGraph") CompiledGraph compiledGraph) {
        this.compiledGraph = compiledGraph;
    }

    @GetMapping("/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "北京时间现在几点钟呀", required = false) String query) throws GraphRunnerException {

        Optional<OverAllState> result = compiledGraph.call(Map.of("messages", new UserMessage(query)));
        List<Message> messages = (List<Message>) result.get().value("messages").get();
        AssistantMessage assistantMessage = (AssistantMessage) messages.get(messages.size() - 1);

        return assistantMessage.getText();
    }
}
