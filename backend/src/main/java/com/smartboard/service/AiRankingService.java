package com.smartboard.service;

import com.smartboard.dto.AiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiRankingService {

    public AiResponse rankTasksWithinQuadrant(String userId, String quadrant) {
        return AiResponse.builder()
                .comingSoon(true)
                .feature("AI Task Ranking")
                .message("Your personal AI ranking engine is coming soon!")
                .preview("Once live, AI will rank your " + quadrant + " tasks based on your " +
                        "strengths, preferred work hours, effort estimates and task history " +
                        "to tell you exactly what to work on first.")
                .data(Map.of("quadrant", quadrant, "userId", userId))
                .build();
    }

    public AiResponse getRankedAllTasks(String userId) {
        return AiResponse.builder()
                .comingSoon(true)
                .feature("AI Full Board Ranking")
                .message("Full AI-powered board ranking is coming soon!")
                .preview("Once live, AI will analyse your entire task board and create a " +
                        "personalised priority order across all quadrants based on your profile, " +
                        "energy levels and deadlines.")
                .data(Map.of("userId", userId))
                .build();
    }
}
