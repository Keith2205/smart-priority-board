package com.smartboard.controller;

import com.smartboard.dto.AiTaskCreationRequest;
import com.smartboard.dto.AiTaskCreationResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.AiTaskCreationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiTaskCreationController {

    private final AiTaskCreationService aiTaskCreationService;
    private final UserRepository userRepository;

    @PostMapping("/create-task")
    public ResponseEntity<AiTaskCreationResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AiTaskCreationRequest request) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(aiTaskCreationService.analyseAndCreateTask(userId, request));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
