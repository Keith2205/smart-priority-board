package com.smartboard.dto;

import com.smartboard.model.WorkHours;
import com.smartboard.model.WorkStyle;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileRequest {

    private List<String> strengths;

    private List<String> weaknesses;

    private WorkHours preferredWorkHours;

    private WorkStyle workStyle;

    private int averageTasksPerDay;
}
