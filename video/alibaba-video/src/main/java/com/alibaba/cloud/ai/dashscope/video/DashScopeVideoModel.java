//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.alibaba.cloud.ai.dashscope.video;

import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi.VideoGenerationRequest;
import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi.VideoGenerationRequest.VideoInput;
import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi.VideoGenerationRequest.VideoParameters;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

public class DashScopeVideoModel implements VideoModel {
    private static final Logger logger = LoggerFactory.getLogger(DashScopeVideoModel.class);
    private final DashScopeVideoApi dashScopeVideoApi;
    private final DashScopeVideoOptions defaultOptions;
    private final RetryTemplate retryTemplate;

    public DashScopeVideoModel(DashScopeVideoApi dashScopeVideoApi, DashScopeVideoOptions defaultOptions, RetryTemplate retryTemplate) {
        Assert.notNull(dashScopeVideoApi, "DashScopeVideoApi must not be null");
        Assert.notNull(defaultOptions, "DashScopeVideoOptions must not be null");
        Assert.notNull(retryTemplate, "RetryTemplate must not be null");
        this.dashScopeVideoApi = dashScopeVideoApi;
        this.defaultOptions = defaultOptions;
        this.retryTemplate = retryTemplate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public VideoResponse call(VideoPrompt prompt) {
        Assert.notNull(prompt, "Prompt must not be null");
        Assert.notEmpty(prompt.getInstructions(), "Prompt instructions must not be empty");
        System.out.println("Video generation task submitted with prompt: " + prompt.getOptions());
        String taskId = this.submitGenTask(prompt);
        if (Objects.isNull(taskId)) {
            return new VideoResponse((DashScopeVideoApi.VideoGenerationResponse)null);
        } else {
            logger.warn("Video generation task submitted with taskId: {}", taskId);
            return (VideoResponse)this.retryTemplate.execute((context) -> {
                DashScopeVideoApi.VideoGenerationResponse resp = this.getVideoTask(taskId);
                if (Objects.nonNull(resp)) {
                    logger.debug(String.valueOf(resp));
                    switch (resp.getOutput().getTaskStatus()) {
                        case "SUCCEEDED":
                            logger.info("Video generation task completed successfully: {}", taskId);
                            return this.toVideoResponse(resp);
                        case "FAILED":
                            logger.error("Video generation task failed: {}", resp.getOutput());
                            return new VideoResponse((DashScopeVideoApi.VideoGenerationResponse)null);
                    }
                }

                throw new TransientAiException("Video generation still pending, retry ...");
            });
        }
    }

    public String submitGenTask(VideoPrompt prompt) {
        DashScopeVideoApi.VideoGenerationRequest request = this.buildDashScopeVideoRequest(prompt);
        DashScopeVideoApi.VideoGenerationResponse response = (DashScopeVideoApi.VideoGenerationResponse)this.dashScopeVideoApi.submitVideoGenTask(request).getBody();
        if (!Objects.isNull(response) && !Objects.isNull(response.getOutput().getTaskId())) {
            return response.getOutput().getTaskId();
        } else {
            logger.warn("Failed to submit video generation task: {}", response);
            return null;
        }
    }

    private DashScopeVideoApi.VideoGenerationResponse getVideoTask(String taskId) {
        ResponseEntity<DashScopeVideoApi.VideoGenerationResponse> videoGenerationResponseResponseEntity = this.dashScopeVideoApi.queryVideoGenTask(taskId);
        if (videoGenerationResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            return (DashScopeVideoApi.VideoGenerationResponse)videoGenerationResponseResponseEntity.getBody();
        } else {
            logger.warn("Failed to query video task: {}", videoGenerationResponseResponseEntity.getStatusCode());
            return null;
        }
    }

    private VideoResponse toVideoResponse(DashScopeVideoApi.VideoGenerationResponse asyncResp) {
        return new VideoResponse(asyncResp);
    }

    private DashScopeVideoApi.VideoGenerationRequest buildDashScopeVideoRequest(VideoPrompt prompt) {
        DashScopeVideoOptions options = this.toVideoOptions(prompt.getOptions());
        logger.debug("Submitting video generation task with options: {}", options);
        return VideoGenerationRequest.builder().model(options.getModel()).input(VideoInput.builder().prompt(((VideoMessage)prompt.getInstructions().get(0)).getText()).negativePrompt(options.getNegativePrompt()).imageUrl(options.getImageUrl()).firstFrameUrl(options.getFirstFrameUrl()).lastFrameUrl(options.getLastFrameUrl()).template(options.getTemplate()).build()).parameters(VideoParameters.builder().duration(options.getDuration()).size(options.getSize()).seed(options.getSeed()).promptExtend(options.getPrompt()).build()).build();
    }

    private DashScopeVideoOptions toVideoOptions(VideoOptions runtimeOptions) {
        DashScopeVideoOptions currentOptions = DashScopeVideoOptions.builder().model(DashScopeVideoOptions.DEFAULT_MODEL).build();
        if (Objects.nonNull(runtimeOptions)) {
            currentOptions = (DashScopeVideoOptions)ModelOptionsUtils.copyToTarget(runtimeOptions, VideoOptions.class, DashScopeVideoOptions.class);
        }

        currentOptions = (DashScopeVideoOptions)ModelOptionsUtils.merge(currentOptions, this.defaultOptions, DashScopeVideoOptions.class);
        return currentOptions;
    }

    public static final class Builder {
        private DashScopeVideoApi videoApi;
        private DashScopeVideoOptions defaultOptions;
        private RetryTemplate retryTemplate;

        private Builder() {
            this.defaultOptions = DashScopeVideoOptions.builder().model(DashScopeVideoOptions.DEFAULT_MODEL).build();
            this.retryTemplate = RetryUtils.DEFAULT_RETRY_TEMPLATE;
        }

        public Builder videoApi(DashScopeVideoApi videoApi) {
            this.videoApi = videoApi;
            return this;
        }

        public Builder defaultOptions(DashScopeVideoOptions defaultOptions) {
            this.defaultOptions = defaultOptions;
            return this;
        }

        public Builder retryTemplate(RetryTemplate retryTemplate) {
            this.retryTemplate = retryTemplate;
            return this;
        }

        public DashScopeVideoModel build() {
            return new DashScopeVideoModel(this.videoApi, this.defaultOptions, this.retryTemplate);
        }
    }
}
