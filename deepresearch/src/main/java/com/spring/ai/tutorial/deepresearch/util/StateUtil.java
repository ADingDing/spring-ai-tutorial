package com.spring.ai.tutorial.deepresearch.util;

import com.alibaba.cloud.ai.graph.OverAllState;

/**
 * @author yingzi
 * @since 2025/6/9
 */

public class StateUtil {

    public static String getQuery(OverAllState state) {
        return state.value("query", "草莓蛋糕怎么做呀");
    }
}
