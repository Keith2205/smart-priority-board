package com.smartboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAiStatus() {
        List<Map<String, String>> features = List.of(
                Map.of(
                        "name", "AI Task Ranking",
                        "endpoint", "/ai/rank",
                        "status", "coming_soon",
                        "description", "Ranks tasks within each quadrant using your profile"
                ),
                Map.of(
                        "name", "AI Task Creation",
                        "endpoint", "/ai/create-task",
                        "status", "coming_soon",
                        "description", "Creates fully configured tasks from just a title and description"
                ),
                Map.of(
                        "name", "AI Quadrant Assistant",
                        "endpoint", "/ai/check-quadrant",
                        "status", "coming_soon",
                        "description", "Warns you when your quadrant choice may be wrong"
                ),
                Map.of(
                        "name", "Daily Focus List",
                        "endpoint", "/ai/focus-list",
                        "status", "coming_soon",
                        "description", "Picks your top 3 tasks to focus on today"
                ),
                Map.of(
                        "name", "Pattern Recognition",
                        "endpoint", "/patterns/suggestions",
                        "status", "coming_soon",
                        "description", "Suggests recurring tasks based on your habits"
                )
        );

        Map<String, Object> response = Map.of(
                "status", "coming_soon",
                "features", features,
                "message", "AI features are currently in development. The Smart Priority Board " +
                        "AI engine will personalise your productivity experience based on your " +
                        "strengths, work patterns and task history.",
                "expectedLaunch", "Phase 2"
        );

        return ResponseEntity.ok(response);
    }
}
