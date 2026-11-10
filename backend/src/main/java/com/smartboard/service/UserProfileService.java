package com.smartboard.service;

import com.smartboard.dto.UserProfileRequest;
import com.smartboard.dto.UserProfileResponse;
import com.smartboard.exception.ResourceNotFoundException;
import com.smartboard.model.UserProfile;
import com.smartboard.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileResponse createOrUpdateProfile(String userId, UserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().userId(userId).build());

        profile.setStrengths(request.getStrengths());
        profile.setWeaknesses(request.getWeaknesses());
        profile.setPreferredWorkHours(request.getPreferredWorkHours());
        profile.setWorkStyle(request.getWorkStyle());
        if (request.getAverageTasksPerDay() > 0) {
            profile.setAverageTasksPerDay(request.getAverageTasksPerDay());
        }

        return toResponse(userProfileRepository.save(profile));
    }

    public UserProfileResponse getProfile(String userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));
        return toResponse(profile);
    }

    // -------------------------------------------------------------------------

    private UserProfileResponse toResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .strengths(profile.getStrengths())
                .weaknesses(profile.getWeaknesses())
                .preferredWorkHours(profile.getPreferredWorkHours())
                .workStyle(profile.getWorkStyle())
                .averageTasksPerDay(profile.getAverageTasksPerDay())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
