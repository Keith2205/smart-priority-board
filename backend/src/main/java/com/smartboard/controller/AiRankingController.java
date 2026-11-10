package com.smartboard.controller;

import com.smartboard.dto.AiResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.AiRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiRankingController {

    private final AiRankingService aiRankingService;
    private final UserRepository userRepository;

    @GetMapping("/rank/{quadrant}")
    public ResponseEntity<AiResponse> rankWithinQuadrant(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String quadrant) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(aiRankingService.rankTasksWithinQuadrant(userId, quadrant));
    }

    @GetMapping("/rank")
    public ResponseEntity<AiResponse> rankAllTasks(
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(aiRankingService.getRankedAllTasks(userId));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
