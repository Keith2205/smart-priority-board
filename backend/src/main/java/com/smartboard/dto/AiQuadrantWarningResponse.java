package com.smartboard.dto;

import com.smartboard.model.Quadrant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiQuadrantWarningResponse {

    @Builder.Default
    private boolean comingSoon = true;

    private String feature;
    private String message;
    private String preview;

    private Quadrant userSelectedQuadrant;

    /** Null until the real AI integration is live. */
    private Quadrant aiSuggestedQuadrant;

    private String warningMessage;

    @Builder.Default
    private boolean shouldWarn = false;
}
