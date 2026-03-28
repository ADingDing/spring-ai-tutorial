package com.spring.ai.tutorial.deepresearch.util;

import com.spring.ai.tutorial.deepresearch.model.constant.PromptPathConstant;

/**
 * @author yingzi
 * @since 2025/10/1
 */

public class PromptUtil {

    public static String loadCoordinatorPrompt() {
        String filePath = PromptPathConstant.COORDINATOR_PROMPT_PATH;
        return ResourceUtil.loadPromptByFilePath(filePath);
    }

    public static String loadRewriteMultiQueryPrompt() {
        String filePath = PromptPathConstant.REWRITE_MULTI_QUERY_PROMPT_PATH;
        return ResourceUtil.loadPromptByFilePath(filePath);
    }
}
