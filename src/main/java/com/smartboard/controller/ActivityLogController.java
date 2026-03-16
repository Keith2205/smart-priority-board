package com.smartboard.controller;

import com.smartboard.dto.ActivityLogResponse;
import com.smartboard.model.User;
import com.smartboard.repository.TaskLogRepository;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/activity-log")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final TaskLogRepository taskLogRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ActivityLogResponse> getActivityLog(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(activityLogService.getActivityLog(userId));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearActivityLog(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = resolveUserId(userDetails);
        String message = activityLogService.clearActivityLog(userId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = resolveUserId(userDetails);
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long count = taskLogRepository.countByUserIdAndCreatedAtAfter(userId, since);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // -------------------------------------------------------------------------

    private String resolveUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
