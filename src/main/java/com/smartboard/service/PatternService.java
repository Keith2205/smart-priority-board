package com.smartboard.service;

import com.smartboard.model.Task;
import com.smartboard.model.TaskPattern;
import com.smartboard.repository.TaskPatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternService {

    private final TaskPatternRepository taskPatternRepository;

    /**
     * Called after every task creation. Normalises the title to a pattern key,
     * then upserts the pattern record with refreshed statistics.
     */
    public void recordTaskCreation(String userId, Task task) {
        String patternKey = normalise(task.getTitle());
        String dayOfWeek  = LocalDateTime.now().getDayOfWeek().name();

        TaskPattern pattern = taskPatternRepository
                .findByUserIdAndPatternKey(userId, patternKey)
                .orElseGet(() -> TaskPattern.builder()
                        .userId(userId)
                        .patternKey(patternKey)
                        .build());

        pattern.setOccurrences(pattern.getOccurrences() + 1);
        pattern.setLastCreatedAt(LocalDateTime.now());
        pattern.setTypicalDayOfWeek(dayOfWeek);
        pattern.setSuggestedTitle(task.getTitle());

        if (task.getEffortEstimate() != null) {
            pattern.setSuggestedEffort(task.getEffortEstimate());
        }
        pattern.setSuggestedIsImportant(Boolean.TRUE.equals(task.getIsImportant()));
        pattern.setSuggestedIsUrgent(Boolean.TRUE.equals(task.getIsUrgent()));

        if (task.getDeadline() != null) {
            long daysUntilDeadline = ChronoUnit.DAYS.between(
                    LocalDateTime.now(), task.getDeadline());
            pattern.setTypicalDeadlineDays((int) Math.max(0, daysUntilDeadline));
        }

        taskPatternRepository.save(pattern);
    }

    /**
     * Returns patterns that match today's day of week and have been seen
     * at least 5 times — these are strong enough signals to surface as
     * suggestions to the user.
     */
    public List<TaskPattern> getSuggestionsForToday(String userId) {
        String today = LocalDateTime.now().getDayOfWeek().name();

        List<TaskPattern> byDay = taskPatternRepository
                .findByUserIdAndTypicalDayOfWeek(userId, today);

        return byDay.stream()
                .filter(p -> p.getOccurrences() >= 5 && p.isActive())
                .toList();
    }

    // -------------------------------------------------------------------------

    private String normalise(String title) {
        return title == null ? "" : title.trim().toLowerCase();
    }
}
