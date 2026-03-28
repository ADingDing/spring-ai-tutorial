package com.spring.ai.tutorial.deepresearch.model.constant;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public enum GraphNodeConstant {

    COORDINATOR("coordinator", "意图识别"),
    REWRITE_MULTI_QUERY("rewrite_multi_query", "优化并扩展查询"),
    BACKGROUND_INVESTIGATION("background_investigation", "背景调查"),
        ;

    private String value;

    private String desc;

    GraphNodeConstant(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
