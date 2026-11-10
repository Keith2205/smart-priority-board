package com.smartboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task_patterns")
@CompoundIndex(name = "userId_patternKey", def = "{'userId': 1, 'patternKey': 1}", unique = true)
public class TaskPattern {

    @Id
    private String id;

    private String userId;

    /** Normalized (lowercase, trimmed) task title used as the lookup key */
    private String patternKey;

    @Builder.Default
    private int occurrences = 0;

    private LocalDateTime lastCreatedAt;

    /** Day of week this task is most commonly created, e.g. "FRIDAY" */
    private String typicalDayOfWeek;

    /** Average number of days between creation and deadline */
    private int typicalDeadlineDays;

    private String suggestedTitle;

    private EffortEstimate suggestedEffort;

    private boolean suggestedIsImportant;

    private boolean suggestedIsUrgent;

    @Builder.Default
    private boolean active = true;
}
