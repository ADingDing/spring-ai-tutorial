package com.alibaba.cloud.ai.mcp.discovery.client.utils;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public class CommonUtil {

    public static String connectedClientName(String clientName, String serverConnectionName) {
        return clientName + " - " + serverConnectionName;
    }
}
