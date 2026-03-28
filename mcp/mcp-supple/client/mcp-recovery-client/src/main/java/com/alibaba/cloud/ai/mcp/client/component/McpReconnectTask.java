package com.alibaba.cloud.ai.mcp.client.component;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class McpReconnectTask implements Delayed {

    private final String serverName;

    private final long delay;

    public McpReconnectTask(String serverName, long delay, TimeUnit unit) {
        this.serverName = serverName;
        this.delay = System.nanoTime() + unit.toNanos(delay);
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delay - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.delay, ((McpReconnectTask) o).delay);
    }

}