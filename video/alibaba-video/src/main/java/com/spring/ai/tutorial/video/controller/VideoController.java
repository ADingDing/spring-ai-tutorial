package com.spring.ai.tutorial.video.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoOptions;
import com.alibaba.cloud.ai.dashscope.video.VideoModel;
import com.alibaba.cloud.ai.dashscope.video.VideoPrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @since 2025/9/28
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoModel videoModel;

    public VideoController(VideoModel videoModel) {

        this.videoModel = videoModel;
    }

    /**
     * 文本 -> 视频
     */
    @GetMapping("/text-to-video")
    public String textToVideo(@RequestParam(value = "prompt", required = false, defaultValue = "皮卡丘和兔子在打架") String prompt) {
        return videoModel.call(new VideoPrompt(prompt)).getResult().getOutput().getVideoUrl();
    }

    /**
     * 文本 + 首帧 -> 视频
     */
    @GetMapping("/text/first-frame-to-video")
    public String textFirstFrameToVideo(@RequestParam(value = "prompt", required = false, defaultValue = "皮卡丘在草地上跳跃到空中") String prompt,
                                        @RequestParam(value = "firstFrame", required = false, defaultValue = "https://yingziimage.oss-cn-beijing.aliyuncs.com/img/image-20250928233724641.png") String firstFrame) {

        return videoModel.call(new VideoPrompt(
                        prompt,
                                DashScopeVideoOptions.builder()
                                        .model(DashScopeVideoApi.VideoModel.WANX2_1_I2V_TURBO.getValue())
                                        .imageUrl(firstFrame)
                                        .build()
                        )
                ).getResult()
                .getOutput()
                .getVideoUrl();
    }

    /**
     * 文本 + 首尾帧 -> 视频
     */
    @GetMapping("/text/first-last-frame-to-video")
    public String textFirstLastFrameToVideo(@RequestParam(value = "prompt", required = false, defaultValue = "皮卡丘在草地上跳跃到空中，放出十万伏特") String prompt,
                                            @RequestParam(value = "firstFrame", required = false, defaultValue = "https://yingziimage.oss-cn-beijing.aliyuncs.com/img/image-20250928233724641.png") String firstFrame,
                                            @RequestParam(value = "lastFrame", required = false, defaultValue = "https://yingziimage.oss-cn-beijing.aliyuncs.com/img/image-20250928234411614.png") String lastFrame) {
        return videoModel.call(new VideoPrompt(
                                prompt,
                                DashScopeVideoOptions.builder()
                                        .model(DashScopeVideoApi.VideoModel.WANX2_1_KF2V_PLUS.getValue())
                                        .firstFrameUrl(firstFrame)
                                        .lastFrameUrl(lastFrame)
                                        .build()
                        )
                ).getResult()
                .getOutput()
                .getVideoUrl();
    }
}
