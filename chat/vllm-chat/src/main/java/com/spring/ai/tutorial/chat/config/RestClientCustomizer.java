package com.spring.ai.tutorial.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

/**
 * @program: spring-ai-tutorial
 * @description: 配置类
 * @author: ding
 * @create: 2025-07-09 17:15
 **/
@Component
public class RestClientCustomizer {
    @Bean
    @Primary
    public RestClient.Builder customRestClientBuilder() {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        System.out.println("httpClient版本" + httpClient.version());
        return RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(httpClient));
    }

    @Bean
    @Primary
    public ClientHttpConnector clientHttpConnector() {
        return new JdkClientHttpConnector(HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build());
    }
}