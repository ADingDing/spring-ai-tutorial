package com.spring.ai.tutorial.component;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ai.tutorial.model.OpenApiDoc;
import com.spring.ai.tutorial.utils.JSONSchemaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.RestfulToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.DynamicMcpToolsProvider;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @since 2025/7/7
 */

public class DynamicRestfulToolsWatch implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRestfulToolsWatch.class);

    private final NamingService namingService;

    private final DynamicMcpToolsProvider dynamicMcpToolsProvider;

    private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, List<RestfulToolDefinition>> service2tool = new HashMap<>();

    private static final String API_DOC_URL = "/v3/api-docs";

    public DynamicRestfulToolsWatch(NamingService namingService, DynamicMcpToolsProvider dynamicMcpToolsProvider, WebClient webClient) {
        this.namingService = namingService;
        this.dynamicMcpToolsProvider = dynamicMcpToolsProvider;
        this.webClient = webClient;
    }

    public void subscribe(String serviceName) {
        try {
            namingService.subscribe(serviceName, this);
        } catch (NacosException e) {
            logger.error("Failed to subscribe service: {}", serviceName, e);
        }
    }


    private void parse(String serviceName) {
        List<Instance> instances;
        try {
            instances = namingService.selectInstances(serviceName, true);
        } catch (NacosException e) {
            logger.error("Failed to get instances for service: {}", serviceName, e);
            return;
        }
        if (instances.isEmpty()) {
            logger.warn("No available service instance for {}", serviceName);
            return;
        }
        // 取出最后一个注册的实例
        Instance selectInstance = instances.stream()
                .max(Comparator.comparingLong(instance -> {
                    String timestamp = instance.getMetadata().getOrDefault("register.timestamp", "0");
                    return Long.parseLong(timestamp);
                }))
                .orElse(instances.get(0)); // 默认回退到第一个实例;
        String url = selectInstance.getMetadata().getOrDefault("scheme", "http") + "://" + selectInstance.getIp() + ":"
                + selectInstance.getPort();
        url = url + API_DOC_URL;
        String apiDocJson = webClient.get().uri(url)
                .retrieve()
                .bodyToMono(String.class).block();

        OpenApiDoc openApiDoc;
        try {
            openApiDoc = objectMapper.readValue(apiDocJson, OpenApiDoc.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse OpenAPI doc for service: {}", serviceName, e);
            return;
        }
        Map<String, OpenApiDoc.PathItem> paths = openApiDoc.paths();

        List<RestfulToolDefinition> toolDefinitions = service2tool.get(serviceName);
        paths.forEach(
                (path, pathItem) -> {
                    boolean addFlag = false;
                    String toolName = pathItem.getOperation().methodName();

                    // 从toolDefinitions中过滤得到方法名为path的toolDefinition
                    RestfulToolDefinition restfulToolDefinition = toolDefinitions.stream()
                            .filter(toolDefinition -> toolDefinition.name().equals(toolName)).toList().get(0);
                    if (restfulToolDefinition == null) {
                        addFlag = true;
                    } else if (restfulToolDefinition.description() != pathItem.getOperation().description() ||
                            restfulToolDefinition.inputSchema() != JSONSchemaUtil.getInputSchema(pathItem.getOperation().parameters()) ||
                            restfulToolDefinition.path() != path)
                    {
                        // 有变化，先删除工具
                        dynamicMcpToolsProvider.removeTool(toolName);
                        addFlag = true;
                    }
                    if (addFlag) { // 如果有变化，则需要更新
                        logger.info("Tool definition changed for service: {}, toolName: {}", serviceName, toolName);
                        ToolDefinition toolDefinition = RestfulToolDefinition.builder()
                                .name(toolName)
                                .description(pathItem.getOperation().description())
                                .inputSchema(JSONSchemaUtil.getInputSchema(pathItem.getOperation().parameters()))
                                .method(toolName)
                                .path(path)
                                .httpMethod(HttpMethod.GET)
                                .serviceName(serviceName)
                                .build();

                        // 增加工具
                        dynamicMcpToolsProvider.addTool(toolDefinition);                    }
                }
        );
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NamingEvent namingEvent) {
            logger.info("Received service instance change event for service: {}", namingEvent.getServiceName());
            List<Instance> instances = namingEvent.getInstances();
            logger.info("Updated instances count: {}", instances.size());
            // 打印每个实例的详细信息
            instances.forEach(instance -> {
                logger.info("Instance: {}:{} (Healthy: {}, Enabled: {}, Metadata: {})", instance.getIp(),
                        instance.getPort(), instance.isHealthy(), instance.isEnabled(),
                        JacksonUtils.toJson(instance.getMetadata()));
            });
            if (!CollectionUtils.isEmpty(instances)) {
                parse(namingEvent.getServiceName());
            }
        }
    }
}
