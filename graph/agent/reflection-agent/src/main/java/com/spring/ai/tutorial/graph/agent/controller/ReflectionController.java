package com.spring.ai.tutorial.graph.agent.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.agent.ReflectAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/8/24
 */
@RestController
@RequestMapping("/reflection")
public class ReflectionController {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionController.class);

    private CompiledGraph compiledGraph;

    public ReflectionController(@Qualifier("reflectionGraph") CompiledGraph compiledGraph) {
        this.compiledGraph = compiledGraph;
    }

    @GetMapping("/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "人工智能社会发展中起到的作用", required = false) String query) throws GraphRunnerException {
        return compiledGraph.call(Map.of(ReflectAgent.MESSAGES, List.of(new UserMessage(query)))).flatMap(invoke -> invoke
                        .<List<Message>>value(ReflectAgent.MESSAGES))
                .orElseThrow()
                .stream()
                .filter(message -> message.getMessageType() == MessageType.ASSISTANT)
                .reduce((first, second) -> second)
                .map(Message::getText)
                .orElseThrow();
    }
}
