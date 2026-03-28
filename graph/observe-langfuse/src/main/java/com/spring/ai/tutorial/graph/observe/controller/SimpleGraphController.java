package com.spring.ai.tutorial.graph.observe.controller;

import com.alibaba.cloud.ai.graph.CompileConfig;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yingzi
 * @since 2025/6/13
 */
@RestController
@RequestMapping("/graph")
public class SimpleGraphController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleGraphController.class);

    private final CompiledGraph compiledGraph;

    public SimpleGraphController(@Qualifier("simpleGraph") StateGraph stateGraph, CompileConfig observationCompileConfig) throws GraphStateException {
        this.compiledGraph = stateGraph.compile(observationCompileConfig);
    }

    @GetMapping(value = "/expand")
    public Map<String, Object> expand(@RequestParam(value = "query", defaultValue = "你好，我的外号是影子，请记住呀！", required = false) String query,
                                                @RequestParam(value = "expander_number", defaultValue = "3", required = false) Integer  expanderNumber,
                                                @RequestParam(value = "thread_id", defaultValue = "yingzi", required = false) String threadId) throws GraphRunnerException {
        RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).build();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("query", query);
        objectMap.put("expander_number", expanderNumber);
        Optional<OverAllState> invoke = this.compiledGraph.call(objectMap, runnableConfig);
        return invoke.map(OverAllState::data).orElse(new HashMap<>());
    }}
