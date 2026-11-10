package com.smartboard.controller;

import com.smartboard.dto.PagedResponse;
import com.smartboard.dto.TaskRequest;
import com.smartboard.dto.TaskResponse;
import com.smartboard.dto.TaskStatusUpdateRequest;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.TaskStatus;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<PagedResponse<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatus status) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(taskService.getAllTasks(userId, page, size, status));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TaskRequest request) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(taskService.getTaskById(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id,
            @Valid @RequestBody TaskRequest request) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(taskService.updateTask(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {

        String userId = resolveUserId(userDetails);
        taskService.deleteTask(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id,
            @Valid @RequestBody TaskStatusUpdateRequest request) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(taskService.updateTaskStatus(userId, id, request));
    }

    // -------------------------------------------------------------------------
    // Helper: resolve the MongoDB userId from the JWT-authenticated email
    // -------------------------------------------------------------------------

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
