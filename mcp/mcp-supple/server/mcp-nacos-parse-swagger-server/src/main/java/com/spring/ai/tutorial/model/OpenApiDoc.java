package com.spring.ai.tutorial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenApiDoc(
        @JsonProperty("paths")
        Map<String, PathItem> paths
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PathItem(
            @JsonProperty("get")
            Operation getOperation,
            @JsonProperty("post")
            Operation postOperation
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Operation(
                @JsonProperty("operationId")
                String methodName,
                @JsonProperty("summary")
                String description,
                @JsonProperty("parameters")
                List<Parameter> parameters

        ){}
    }
}