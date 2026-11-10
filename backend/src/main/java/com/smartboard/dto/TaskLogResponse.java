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
public class TaskLogResponse {

    private String id;
    private String userId;
    private String originalTaskId;
    private String title;
    private String description;
    private Quadrant quadrant;
    private Priority priority;
    private TaskStatus status;
    private EffortEstimate effortEstimate;
    private LocalDateTime deadline;
    private List<String> tags;
    private LocalDateTime completedAt;
    private LocalDateTime autoDeletedAt;
    private LocalDateTime createdAt;
}
