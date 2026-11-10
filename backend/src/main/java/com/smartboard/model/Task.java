package com.smartboard.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    @NotBlank
    private String title;

    private String description;

    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;

    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<String> subtasks = new ArrayList<>();

    // ---- Eisenhower Matrix fields ----

    private LocalDateTime deadline;

    @Builder.Default
    private Boolean isImportant = false;

    @Builder.Default
    private Boolean isUrgent = false;

    private EffortEstimate effortEstimate;

    /** Auto-calculated from isImportant + isUrgent */
    private Quadrant quadrant;

    /** What the AI thinks the quadrant should be (may differ from user's choice) */
    private Quadrant aiSuggestedQuadrant;

    /** Explanation of why the AI disagrees with the user's quadrant assignment */
    private String aiQuadrantReason;

    /** True if this task was created through AI-assisted mode */
    @Builder.Default
    private boolean createdByAi = false;

    // ---- Status tracking fields ----

    private LocalDateTime completedAt;

    private LocalDateTime statusUpdatedAt;

    // ---- Audit fields ----

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Indexed
    private String userId;
}
