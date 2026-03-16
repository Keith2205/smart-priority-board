package com.smartboard.dto;

import com.smartboard.model.EffortEstimate;
import com.smartboard.model.Quadrant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiQuadrantWarningRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "User selected quadrant is required")
    private Quadrant userSelectedQuadrant;

    private Boolean isImportant;

    private Boolean isUrgent;

    private EffortEstimate effortEstimate;

    private LocalDateTime deadline;
}
