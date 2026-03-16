package com.smartboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiTaskCreationRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}
