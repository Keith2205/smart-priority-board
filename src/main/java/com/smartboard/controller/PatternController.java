package com.smartboard.controller;

import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.TaskPattern;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.PatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patterns")
@RequiredArgsConstructor
public class PatternController {

    private final PatternService patternService;
    private final UserRepository userRepository;

    @GetMapping("/suggestions")
    public ResponseEntity<List<TaskPattern>> getSuggestionsForToday(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(patternService.getSuggestionsForToday(userId));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
