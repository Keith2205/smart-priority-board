package com.smartboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task_logs")
public class TaskLog {

    @Id
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

    @CreatedDate
    private LocalDateTime createdAt;
}
