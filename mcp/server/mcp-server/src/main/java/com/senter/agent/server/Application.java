package com.senter.agent.server;

import com.senter.agent.server.service.DospAllService;
import com.senter.agent.server.service.DospNewService;
import com.senter.agent.server.service.NWService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author yHong
 * @version 1.0
 * @since 2025/4/21 20:00
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //    @Bean
    public ToolCallbackProvider tools(DospAllService dospAllService, DospNewService dospNewService) {
        return MethodToolCallbackProvider.builder().toolObjects(dospAllService, dospNewService).build();
    }

    @Bean
    public ToolCallbackProvider toolsNW(NWService nwService) {
        return MethodToolCallbackProvider.builder().toolObjects(nwService).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
