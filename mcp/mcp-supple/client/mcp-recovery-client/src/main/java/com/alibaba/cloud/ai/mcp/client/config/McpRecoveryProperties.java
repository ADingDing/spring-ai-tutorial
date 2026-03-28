package com.alibaba.cloud.ai.mcp.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = McpRecoveryProperties.CONFIG_PREFIX)
public class McpRecoveryProperties {

    public static final String CONFIG_PREFIX = "spring.ai.alibaba.mcp.recovery";

    private boolean enabled = false;

    private Duration ping = Duration.ofSeconds(5L);

    private Duration delay = Duration.ofSeconds(5L);

    private Duration stop = Duration.ofSeconds(10L);

    private boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getPing() {
        return ping;
    }

    public void setPing(Duration ping) {
        this.ping = ping;
    }

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public Duration getStop() {
        return stop;
    }

    public void setStop(Duration stop) {
        this.stop = stop;
    }

}