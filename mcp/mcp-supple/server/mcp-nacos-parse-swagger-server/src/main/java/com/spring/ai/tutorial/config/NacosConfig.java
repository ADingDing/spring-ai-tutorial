package com.spring.ai.tutorial.config;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author yingzi
 * @since 2025/7/7
 */
@Configuration
public class NacosConfig {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.discovery.username}")
    private String username;
    @Value("${spring.cloud.nacos.discovery.password}")
    private String password;

    @Bean
    public NamingService namingService() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.NAMESPACE, Objects.toString(this.namespace, ""));
        properties.put(PropertyKeyConst.SERVER_ADDR, Objects.toString(this.serverAddr, ""));
        properties.put(PropertyKeyConst.USERNAME, Objects.toString(this.username, ""));
        properties.put(PropertyKeyConst.PASSWORD, Objects.toString(this.password, ""));

        return NamingFactory.createNamingService(properties);
    }

}
