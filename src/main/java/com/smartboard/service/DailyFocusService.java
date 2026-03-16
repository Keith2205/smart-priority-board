package com.smartboard.service;

import com.smartboard.dto.FocusListResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class DailyFocusService {

    public FocusListResponse getDailyFocusList(String userId) {
        return FocusListResponse.builder()
                .comingSoon(true)
                .feature("Daily Focus List")
                .message("Your AI-powered daily focus list is coming soon!")
                .preview("Once live, every morning your AI assistant will analyse your full task " +
                        "board, your energy patterns and today's deadlines to pick the top 3 tasks " +
                        "you should focus on today. No more decision fatigue.")
                .date(LocalDate.now())
                .focusTasks(Collections.emptyList())
                .motivationalMessage("Great things are built one focused day at a time.")
                .build();
    }
}
