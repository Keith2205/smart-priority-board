package com.smartboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiTaskCreationResponse {

    @Builder.Default
    private boolean comingSoon = true;

    private String feature;
    private String message;
    private String preview;

    /** Preview of the task the AI would create, populated with realistic placeholders. */
    private TaskResponse suggestedTask;
}
