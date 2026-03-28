package com.spring.ai.tutorial.deepresearch.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * @author yingzi
 * @since 2025/5/17 18:10
 */
@Service
public class PlannerTool {

    private static final Logger logger = LoggerFactory.getLogger(PlannerTool.class);

    @Tool(name = "handoff_to_planner", description = "Handoff to planner agent to do plan.")
    public void handoffToPlanner(String taskTitle) {
        // This method is not returning anything. It is used as a way for LLM
        // to signal that it needs to hand off to the planner agent.
        logger.info("🔧 Handoff to planner task: {}", taskTitle);
    }

}
