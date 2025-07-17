package com.spring.ai.tutorial;

import com.spring.ai.tutorial.config.Swagger2Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author yingzi
 * @since 2025/7/7
 */
@SpringBootApplication
public class NacosSwaggerApplication {

    private static final Logger logger = LoggerFactory.getLogger(NacosSwaggerApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NacosSwaggerApplication.class, args);

        // 从Spring上下文中获取ServerPortListener实例
        ServerPortListener serverPortListener = context.getBean(ServerPortListener.class);
        int port = serverPortListener.getPort();

        // 从Spring上下文中获取SwaggerConfigProperties实例
        Swagger2Config.SwaggerConfigProperties swaggerConfigProperties = context.getBean(Swagger2Config.SwaggerConfigProperties.class);
        String swaggerPath = swaggerConfigProperties.getPath();

        // SwaggerUI访问地址
        String swaggerUrl = "http://localhost:" + port + swaggerPath;
        logger.info("SwaggerUI访问地址: {}", swaggerUrl);
    }

    // 新增一个内部类用于监听端口初始化事件
    @Component
    static class ServerPortListener implements ApplicationListener<WebServerInitializedEvent> {
        private int port;

        @Override
        public void onApplicationEvent(WebServerInitializedEvent event) {
            this.port = event.getWebServer().getPort();
        }

        public int getPort() {
            return this.port;
        }
    }
}
