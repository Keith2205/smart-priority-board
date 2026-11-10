package com.smartboard.dto;

import com.smartboard.model.EffortEstimate;
import com.smartboard.model.Priority;
import com.smartboard.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private List<String> tags;

    private List<String> subtasks;

    // ---- Eisenhower Matrix fields ----

    private LocalDateTime deadline;

    private Boolean isImportant;

    private Boolean isUrgent;

    private EffortEstimate effortEstimate;

    private boolean createdByAi;
}
