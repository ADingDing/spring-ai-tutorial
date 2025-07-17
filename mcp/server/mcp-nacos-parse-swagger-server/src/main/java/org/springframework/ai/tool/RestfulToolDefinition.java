package org.springframework.ai.tool;

import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.support.ToolUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author yingzi
 * @since 2025/7/9
 */

public record RestfulToolDefinition(
        String name,
        String description,
        String inputSchema,
        String method,
        String path,
        HttpMethod httpMethod,
        String serviceName
) implements ToolDefinition {

    public RestfulToolDefinition {
        Assert.hasText(name, "name cannot be null or empty");
        Assert.hasText(description, "description cannot be null or empty");
        Assert.hasText(inputSchema, "inputSchema cannot be null or empty");
        Assert.hasText(method, "method cannot be null or empty");
        Assert.hasText(path, "path cannot be null or empty");
        Assert.notNull(httpMethod, "httpMethod cannot be null");
        Assert.hasText(serviceName, "serviceName cannot be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String inputSchema;
        private String method;
        private String path;
        private HttpMethod httpMethod;
        private String serviceName;


        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder inputSchema(String inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }


        public ToolDefinition build() {
            if (!StringUtils.hasText(this.description)) {
                this.description = ToolUtils.getToolDescriptionFromName(this.name);
            }

            return new RestfulToolDefinition(
                    this.name,
                    this.description,
                    this.inputSchema,
                    this.method,
                    this.path,
                    this.httpMethod,
                    this.serviceName
            );
        }
    }
}
