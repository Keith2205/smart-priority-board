package com.smartboard.controller;

import com.smartboard.dto.UserProfileRequest;
import com.smartboard.dto.UserProfileResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.User;
import com.smartboard.repository.UserRepository;
import com.smartboard.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(userProfileService.getProfile(userId));
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createOrUpdateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfileRequest request) {
        String userId = resolveUserId(userDetails);
        return ResponseEntity.ok(userProfileService.createOrUpdateProfile(userId, request));
    }

    private String resolveUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return user.getId();
    }
}
