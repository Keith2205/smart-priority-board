package com.smartboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FocusListResponse {

    @Builder.Default
    private boolean comingSoon = true;

    private String feature;
    private String message;
    private String preview;

    private LocalDate date;

    /** Top tasks the AI would recommend for today — empty until AI is live. */
    private List<TaskResponse> focusTasks;

    private String motivationalMessage;
}
