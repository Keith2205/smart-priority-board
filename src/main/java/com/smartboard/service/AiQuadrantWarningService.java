package com.smartboard.service;

import com.smartboard.dto.AiQuadrantWarningRequest;
import com.smartboard.dto.AiQuadrantWarningResponse;
import org.springframework.stereotype.Service;

@Service
public class AiQuadrantWarningService {

    public AiQuadrantWarningResponse checkQuadrant(String userId,
                                                    AiQuadrantWarningRequest request) {
        return AiQuadrantWarningResponse.builder()
                .comingSoon(true)
                .feature("AI Quadrant Assistant")
                .message("AI quadrant validation is coming soon!")
                .preview("Once live, when you place a task in a quadrant, our AI will check " +
                        "your decision against your profile and history. If it thinks you are " +
                        "underestimating a task it will warn you — just like a smart colleague would.")
                .userSelectedQuadrant(request.getUserSelectedQuadrant())
                .aiSuggestedQuadrant(null)
                .warningMessage("AI analysis coming soon")
                .shouldWarn(false)
                .build();
    }
}
