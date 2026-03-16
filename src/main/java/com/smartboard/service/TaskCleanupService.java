package com.smartboard.service;

import com.smartboard.model.TaskLog;
import com.smartboard.model.TaskStatus;
import com.smartboard.repository.TaskLogRepository;
import com.smartboard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskCleanupService {

    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;

    /**
     * Runs every hour. Finds DONE tasks completed more than 72 hours ago,
     * archives them to task_logs, then deletes them from tasks.
     */
    @Scheduled(fixedRate = 3_600_000)
    public void cleanupCompletedTasks() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(72);

        List<com.smartboard.model.Task> staleDone =
                taskRepository.findByStatusAndCompletedAtBefore(TaskStatus.DONE, cutoff);

        if (staleDone.isEmpty()) {
            log.debug("Task cleanup: no completed tasks eligible for archival.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        for (com.smartboard.model.Task task : staleDone) {
            TaskLog log2 = TaskLog.builder()
                    .userId(task.getUserId())
                    .originalTaskId(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .quadrant(task.getQuadrant())
                    .priority(task.getPriority())
                    .status(task.getStatus())
                    .effortEstimate(task.getEffortEstimate())
                    .deadline(task.getDeadline())
                    .tags(task.getTags())
                    .completedAt(task.getCompletedAt())
                    .autoDeletedAt(now)
                    .build();

            taskLogRepository.save(log2);
            taskRepository.delete(task);
            log.info("Auto-deleted task: {}", task.getTitle());
        }

        log.info("Task cleanup complete. Archived and deleted {} task(s).", staleDone.size());
    }

    /**
     * Runs at 2am daily. Purges task log entries older than 30 days.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOldLogs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<TaskLog> oldLogs = taskLogRepository.findByAutoDeletedAtBefore(cutoff);

        if (oldLogs.isEmpty()) {
            log.debug("Log cleanup: no old log entries to purge.");
            return;
        }

        taskLogRepository.deleteAll(oldLogs);
        log.info("Log cleanup complete. Purged {} log entries older than 30 days.", oldLogs.size());
    }
}
