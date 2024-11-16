package com.rafirvan.minitrack.service;


import com.rafirvan.minitrack.dto.request.UserProfileRequest;
import com.rafirvan.minitrack.dto.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse updateUserProfile(Long userAccountId, UserProfileRequest request);
    UserProfileResponse getUserProfileByUserAccountId(Long userAccountId);
}
