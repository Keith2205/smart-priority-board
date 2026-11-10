package com.smartboard.service;

import com.smartboard.dto.AiTaskCreationRequest;
import com.smartboard.dto.AiTaskCreationResponse;
import com.smartboard.dto.TaskResponse;
import com.smartboard.model.EffortEstimate;
import com.smartboard.model.Priority;
import com.smartboard.model.Quadrant;
import com.smartboard.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiTaskCreationService {

    public AiTaskCreationResponse analyseAndCreateTask(String userId,
                                                        AiTaskCreationRequest request) {
        TaskResponse suggestedTask = TaskResponse.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .priority(Priority.MEDIUM)
                .isImportant(true)
                .isUrgent(false)
                .quadrant(Quadrant.Q2)
                .effortEstimate(EffortEstimate.HOUR_1)
                .tags(List.of("ai-suggested"))
                .subtasks(List.of("AI will generate subtasks when feature goes live"))
                .aiSuggestedQuadrant(Quadrant.Q1)
                .aiQuadrantReason("AI analysis coming soon")
                .createdByAi(true)
                .userId(userId)
                .build();

        return AiTaskCreationResponse.builder()
                .comingSoon(true)
                .feature("AI Task Creation")
                .message("AI-powered task creation is coming soon!")
                .preview("Once live, simply describe your task and our AI will automatically " +
                        "set the priority, quadrant, effort estimate, tags and subtasks based " +
                        "on your profile and work patterns.")
                .suggestedTask(suggestedTask)
                .build();
    }
}
