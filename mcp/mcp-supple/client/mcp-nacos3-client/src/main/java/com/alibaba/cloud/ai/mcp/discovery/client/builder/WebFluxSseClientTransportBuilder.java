package com.alibaba.cloud.ai.mcp.discovery.client.builder;

import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yingzi
 * @since 2025/10/25
 */

public class WebFluxSseClientTransportBuilder {

    public static WebFluxSseClientTransport build(WebClient.Builder webClientBuilder, McpJsonMapper jsonMapper,
                                           String sseEndpoint) {
        return WebFluxSseClientTransport.builder(webClientBuilder)
                .sseEndpoint(sseEndpoint)
                .jsonMapper(jsonMapper)
                .build();
    }

    public static WebFluxSseClientTransport build(WebClient.Builder webClientBuilder, McpJsonMapper jsonMapper,
                                           String sseEndpoint, ExchangeFilterFunction traceFilter) {
        if (traceFilter != null) {
            webClientBuilder.filter(traceFilter);
        }
        return WebFluxSseClientTransport.builder(webClientBuilder)
                .sseEndpoint(sseEndpoint)
                .jsonMapper(jsonMapper)
                .build();
    }
}
