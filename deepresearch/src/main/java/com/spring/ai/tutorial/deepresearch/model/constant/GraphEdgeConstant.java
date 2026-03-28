package com.spring.ai.tutorial.deepresearch.model.constant;

import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;


/**
 * @author yingzi
 * @since 2025/10/1
 */

public enum GraphEdgeConstant {

    COORDINATOR_EDGE("coordinator_next_node",
            Map.of(END, END, GraphNodeConstant.REWRITE_MULTI_QUERY.getValue(), GraphNodeConstant.REWRITE_MULTI_QUERY.getValue()),
            "optional：END、rewrite_multi_query"),

    REWRITE_MULTI_QUERY_EDGE("rewrite_multi_query_next_node",
            Map.of(GraphNodeConstant.BACKGROUND_INVESTIGATION.getValue(), GraphNodeConstant.BACKGROUND_INVESTIGATION.getValue()),
            "optional: background_investigation")
    ;
    private String value;

    private String desc;

    private Map<String, String> mappings;

    private GraphEdgeConstant(String value, Map<String, String> mappings, String desc) {
        this.value = value;
        this.mappings = mappings;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> getMappings() {
        return mappings;
    }
}
