package com.spring.ai.tutorial.deepresearch.model.constant;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public enum GraphStateConstant {

    QUERY("query", "用户问题"),
    OPTIMIZE_QUERIES("optimize_queries", "优化&&扩展后的查询"),

    OUTPUT("output", "输出结果"),;

    private String value;

    private String desc;

    GraphStateConstant(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
