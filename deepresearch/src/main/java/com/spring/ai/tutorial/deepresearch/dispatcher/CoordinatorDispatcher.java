package com.spring.ai.tutorial.deepresearch.dispatcher;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.spring.ai.tutorial.deepresearch.model.constant.GraphEdgeConstant;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public class CoordinatorDispatcher implements EdgeAction {
    @Override
    public String apply(OverAllState state) {
        return state.value(GraphEdgeConstant.COORDINATOR_EDGE.getValue(), END);
    }
}
