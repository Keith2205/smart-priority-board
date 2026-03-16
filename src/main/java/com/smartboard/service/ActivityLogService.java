package com.smartboard.service;

import com.smartboard.dto.ActivityLogResponse;
import com.smartboard.dto.TaskLogResponse;
import com.smartboard.model.TaskLog;
import com.smartboard.repository.TaskLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final TaskLogRepository taskLogRepository;

    public ActivityLogResponse getActivityLog(String userId) {
        List<TaskLog> logs = taskLogRepository.findByUserId(userId);

        List<TaskLogResponse> sorted = logs.stream()
                .sorted(Comparator.comparing(TaskLog::getAutoDeletedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toResponse)
                .toList();

        return ActivityLogResponse.builder()
                .logs(sorted)
                .totalCount(sorted.size())
                .autoCleanupDays(30)
                .message("Tasks completed and auto-removed after 72 hours. " +
                         "Log auto-clears after 30 days.")
                .build();
    }

    public String clearActivityLog(String userId) {
        taskLogRepository.deleteByUserId(userId);
        return "Activity log cleared successfully.";
    }

    // -------------------------------------------------------------------------

    private TaskLogResponse toResponse(TaskLog log) {
        return TaskLogResponse.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .originalTaskId(log.getOriginalTaskId())
                .title(log.getTitle())
                .description(log.getDescription())
                .quadrant(log.getQuadrant())
                .priority(log.getPriority())
                .status(log.getStatus())
                .effortEstimate(log.getEffortEstimate())
                .deadline(log.getDeadline())
                .tags(log.getTags())
                .completedAt(log.getCompletedAt())
                .autoDeletedAt(log.getAutoDeletedAt())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
