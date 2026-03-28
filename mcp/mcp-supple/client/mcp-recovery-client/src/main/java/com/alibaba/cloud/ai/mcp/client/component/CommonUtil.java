package com.alibaba.cloud.ai.mcp.client.component;

import com.alibaba.cloud.ai.mcp.client.config.McpRecoveryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private final McpRecoveryProperties mcpRecoveryProperties;

    private final ScheduledExecutorService pingScheduler = Executors.newSingleThreadScheduledExecutor();

    private final ExecutorService reconnectExecutor = Executors.newSingleThreadExecutor();

    public CommonUtil(McpRecoveryProperties mcpRecoveryProperties1) {
        this.mcpRecoveryProperties = mcpRecoveryProperties1;
    }

    public ScheduledExecutorService getPingScheduler() {
        return pingScheduler;
    }

    public ExecutorService getReconnectExecutor() {
        return reconnectExecutor;
    }

    public static String connectedClientName(String clientName, String serverConnectionName) {
        return clientName + " - " + serverConnectionName;
    }

    public void stop() {
        pingScheduler.shutdown();
        logger.info("pingScheduler stop...");

        // 关闭异步任务线程池
        try {
            reconnectExecutor.shutdown();
            if (!reconnectExecutor.awaitTermination(mcpRecoveryProperties.getStop().getSeconds(), TimeUnit.SECONDS)) {
                reconnectExecutor.shutdownNow();
            }
            logger.info("reconnectExecutor stop successfully");
        }
        catch (InterruptedException e) {
            logger.error("reconnectExecutor stop error", e);
            reconnectExecutor.shutdownNow();
            Thread.currentThread().interrupt(); // 恢复中断状态
        }
    }

}