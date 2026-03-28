//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.ai.mcp.client.autoconfigure.properties;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.ai.mcp.client")
public class McpClientCommonProperties {
    public static final String CONFIG_PREFIX = "spring.ai.mcp.client";
    private boolean enabled = true;
    private String name = "spring-ai-mcp-client";
    private String version = "1.0.0";
    private boolean initialized = true;
    private Duration requestTimeout = Duration.ofSeconds(20L);
    private ClientType type;
    private boolean rootChangeNotification;
    private Toolcallback toolcallback;
    private boolean returnDirect = false;

    public McpClientCommonProperties() {
        this.type = McpClientCommonProperties.ClientType.SYNC;
        this.rootChangeNotification = true;
        this.toolcallback = new Toolcallback();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public Duration getRequestTimeout() {
        return this.requestTimeout;
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public ClientType getType() {
        return this.type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public boolean isRootChangeNotification() {
        return this.rootChangeNotification;
    }

    public void setRootChangeNotification(boolean rootChangeNotification) {
        this.rootChangeNotification = rootChangeNotification;
    }

    public Toolcallback getToolcallback() {
        return this.toolcallback;
    }

    public void setToolcallback(Toolcallback toolcallback) {
        this.toolcallback = toolcallback;
    }

    public boolean isReturnDirect() {
        return this.returnDirect;
    }

    public void setReturnDirect(boolean returnDirect) {
        this.returnDirect = returnDirect;
    }

    public static enum ClientType {
        SYNC,
        ASYNC;
    }

    public static class Toolcallback {
        private boolean enabled = true;

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }
}
