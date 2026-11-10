package com.smartboard.controller;

import com.smartboard.dto.AiQuadrantWarningRequest;
import com.smartboard.dto.AiQuadrantWarningResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.AiQuadrantWarningService;
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
public class AiQuadrantWarningController {

    private final AiQuadrantWarningService aiQuadrantWarningService;
    private final UserRepository userRepository;

    @PostMapping("/check-quadrant")
    public ResponseEntity<AiQuadrantWarningResponse> checkQuadrant(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AiQuadrantWarningRequest request) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(aiQuadrantWarningService.checkQuadrant(userId, request));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
