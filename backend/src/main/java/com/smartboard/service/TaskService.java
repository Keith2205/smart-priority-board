package com.smartboard.service;

import com.smartboard.dto.PagedResponse;
import com.smartboard.dto.TaskRequest;
import com.smartboard.dto.TaskResponse;
import com.smartboard.dto.TaskStatusUpdateRequest;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.exception.UnauthorizedException;
import com.smartboard.model.Priority;
import com.smartboard.model.Quadrant;
import com.smartboard.model.Task;
import com.smartboard.model.TaskStatus;
import com.smartboard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final PatternService patternService;

    // Sort: Q1 → Q2 → Q3 → Q4, then earliest deadline first (nulls last),
    // then smallest effort first (quick wins within same deadline bucket).
    private static final Sort TASK_SORT = Sort.by(
            Sort.Order.asc("quadrant"),           // Q1 < Q2 < Q3 < Q4 (enum ordinal asc)
            Sort.Order.asc("deadline").nullsLast(),
            Sort.Order.asc("effortEstimate").nullsLast()
    );

    public PagedResponse<TaskResponse> getAllTasks(String userId, int page, int size,
                                                   TaskStatus status) {
        Pageable pageable = PageRequest.of(page, size, TASK_SORT);
        Page<Task> taskPage;

        if (status == null) {
            taskPage = taskRepository.findByUserId(userId, pageable);
        } else {
            taskPage = taskRepository.findByUserIdAndStatus(userId, status, pageable);
        }

        List<TaskResponse> content = taskPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return PagedResponse.<TaskResponse>builder()
                .content(content)
                .page(taskPage.getNumber())
                .size(taskPage.getSize())
                .totalElements(taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .last(taskPage.isLast())
                .build();
    }

    public TaskResponse getTaskById(String userId, String taskId) {
        Task task = findTaskOrThrow(taskId);
        verifyOwnership(task, userId);
        return toResponse(task);
    }

    public TaskResponse createTask(String userId, TaskRequest request) {
        boolean isImportant = request.getIsImportant() != null && request.getIsImportant();
        boolean isUrgent    = request.getIsUrgent()    != null && request.getIsUrgent();

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .subtasks(request.getSubtasks() != null ? request.getSubtasks() : new ArrayList<>())
                .deadline(request.getDeadline())
                .isImportant(isImportant)
                .isUrgent(isUrgent)
                .effortEstimate(request.getEffortEstimate())
                .quadrant(Quadrant.calculate(isImportant, isUrgent))
                .createdByAi(request.isCreatedByAi())
                .userId(userId)
                .build();

        Task saved = taskRepository.save(task);
        patternService.recordTaskCreation(userId, saved);
        return toResponse(saved);
    }

    public TaskResponse updateTask(String userId, String taskId, TaskRequest request) {
        Task task = findTaskOrThrow(taskId);
        verifyOwnership(task, userId);

        boolean isImportant = request.getIsImportant() != null
                ? request.getIsImportant()
                : Boolean.TRUE.equals(task.getIsImportant());
        boolean isUrgent = request.getIsUrgent() != null
                ? request.getIsUrgent()
                : Boolean.TRUE.equals(task.getIsUrgent());

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        task.setPriority(request.getPriority() != null ? request.getPriority() : task.getPriority());
        task.setTags(request.getTags() != null ? request.getTags() : task.getTags());
        task.setSubtasks(request.getSubtasks() != null ? request.getSubtasks() : task.getSubtasks());
        task.setDeadline(request.getDeadline() != null ? request.getDeadline() : task.getDeadline());
        task.setIsImportant(isImportant);
        task.setIsUrgent(isUrgent);
        task.setEffortEstimate(request.getEffortEstimate() != null
                ? request.getEffortEstimate() : task.getEffortEstimate());
        task.setQuadrant(Quadrant.calculate(isImportant, isUrgent));

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateTaskStatus(String userId, String taskId,
                                         TaskStatusUpdateRequest request) {
        Task task = findTaskOrThrow(taskId);
        verifyOwnership(task, userId);
        TaskStatus newStatus = request.getStatus();
        task.setStatus(newStatus);
        task.setStatusUpdatedAt(LocalDateTime.now());
        if (newStatus == TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        } else if (task.getCompletedAt() != null) {
            task.setCompletedAt(null);
        }
        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(String userId, String taskId) {
        Task task = findTaskOrThrow(taskId);
        verifyOwnership(task, userId);
        taskRepository.delete(task);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Task findTaskOrThrow(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
    }

    private void verifyOwnership(Task task, String userId) {
        if (!task.getUserId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this task");
        }
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .tags(task.getTags())
                .subtasks(task.getSubtasks())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .userId(task.getUserId())
                .deadline(task.getDeadline())
                .isImportant(task.getIsImportant())
                .isUrgent(task.getIsUrgent())
                .effortEstimate(task.getEffortEstimate())
                .quadrant(task.getQuadrant())
                .aiSuggestedQuadrant(task.getAiSuggestedQuadrant())
                .aiQuadrantReason(task.getAiQuadrantReason())
                .createdByAi(task.isCreatedByAi())
                .completedAt(task.getCompletedAt())
                .statusUpdatedAt(task.getStatusUpdatedAt())
                .build();
    }
}
