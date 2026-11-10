package com.smartboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

    @Builder.Default
    private boolean comingSoon = true;

    private String feature;
    private String message;
    private String preview;

    /** Placeholder data showing the shape of the real future response. */
    private Object data;
}
