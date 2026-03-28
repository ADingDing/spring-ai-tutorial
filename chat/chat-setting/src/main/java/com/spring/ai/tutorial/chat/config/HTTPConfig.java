package com.spring.ai.tutorial.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author yingzi
 * @since 2025/8/28
 */
@Configuration
public class HTTPConfig {

    @Value("${http.client.connect-timeout:60s}")
    private Duration connectTimeout;

    @Value("${http.client.read-timeout:60s}")
    private Duration readTimeout;

    @Bean
    public ClientHttpRequestFactorySettings clientHttpRequestFactorySettings() {
        return new ClientHttpRequestFactorySettings(ClientHttpRequestFactorySettings.Redirects.FOLLOW_WHEN_POSSIBLE, connectTimeout,
                readTimeout, null);
    }
}
