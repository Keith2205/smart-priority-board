package com.smartboard.dto;

import com.smartboard.model.EffortEstimate;
import com.smartboard.model.Priority;
import com.smartboard.model.Quadrant;
import com.smartboard.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private List<String> tags;
    private List<String> subtasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    // ---- Eisenhower Matrix fields ----

    private LocalDateTime deadline;
    private Boolean isImportant;
    private Boolean isUrgent;
    private EffortEstimate effortEstimate;
    private Quadrant quadrant;
    private Quadrant aiSuggestedQuadrant;
    private String aiQuadrantReason;
    private boolean createdByAi;

    // ---- Status tracking ----
    private LocalDateTime completedAt;
    private LocalDateTime statusUpdatedAt;
}
