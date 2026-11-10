package com.smartboard.dto;

import com.smartboard.model.WorkHours;
import com.smartboard.model.WorkStyle;
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
public class UserProfileResponse {

    private String id;
    private String userId;
    private List<String> strengths;
    private List<String> weaknesses;
    private WorkHours preferredWorkHours;
    private WorkStyle workStyle;
    private int averageTasksPerDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
