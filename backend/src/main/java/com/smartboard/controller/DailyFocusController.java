package com.smartboard.controller;

import com.smartboard.dto.FocusListResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.DailyFocusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class DailyFocusController {

    private final DailyFocusService dailyFocusService;
    private final UserRepository userRepository;

    @GetMapping("/focus-list")
    public ResponseEntity<FocusListResponse> getFocusList(
            @AuthenticationPrincipal UserDetails userDetails) {

        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(dailyFocusService.getDailyFocusList(userId));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
