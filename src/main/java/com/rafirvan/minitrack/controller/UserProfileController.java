package com.rafirvan.minitrack.controller;
import com.rafirvan.minitrack.dto.request.UserProfileRequest;
import com.rafirvan.minitrack.dto.response.UserProfileResponse;
import com.rafirvan.minitrack.service.UserProfileService;
import com.rafirvan.minitrack.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Profile Management")
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Get user profile by user account ID")
    @GetMapping("/{userAccountId}")
    public ResponseEntity<?> getUserProfileByUserAccountId(@PathVariable Long userAccountId) {
        UserProfileResponse response = userProfileService.getUserProfileByUserAccountId(userAccountId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "User profile retrieved successfully", response);
    }

    @Operation(summary = "Update user profile")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userAccountId}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long userAccountId,
            @RequestBody UserProfileRequest request) {
        UserProfileResponse response = userProfileService.updateUserProfile(userAccountId, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "User profile updated successfully", response);
    }
}
