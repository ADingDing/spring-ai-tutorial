package com.spring.ai.tutorial.image.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @since 2025/9/28
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/call")
    public ResponseEntity<?> call(@RequestParam(value = "prompt", required = false, defaultValue = "皮卡丘和兔子的结合") String prompt,
                               @RequestParam(value = "count", required = false, defaultValue = "1") int count,
                               @RequestParam(value = "height", required = false, defaultValue = "1024") Integer height,
                               @RequestParam(value = "width", required = false, defaultValue = "1024") Integer width) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .N(count)
                .height(height)
                .width(width)
                .build();
        ImageResponse response = this.imageModel.call(new ImagePrompt(prompt, options));
        return ResponseEntity.ok(response);
    }
}
