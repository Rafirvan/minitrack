package com.rafirvan.minitrack.service.impl;

import com.rafirvan.minitrack.constant.Gender;
import com.rafirvan.minitrack.dto.request.UserProfileRequest;
import com.rafirvan.minitrack.dto.response.UserProfileResponse;
import com.rafirvan.minitrack.service.UserProfileService;
import com.rafirvan.minitrack.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserProfileResponse updateUserProfile(Long userAccountId, UserProfileRequest request) {
        validationUtil.validate(request);

        try{
            int updatedCount = entityManager.createNativeQuery(
                        "UPDATE user_profiles SET full_name = ?, email = ?, gender = ? WHERE user_account_id = ?")
                .setParameter(1, request.getFullName())
                .setParameter(2, request.getEmail())
                .setParameter(3, Gender.valueOf(request.getGender().toUpperCase()).toString())
                .setParameter(4, userAccountId)
                .executeUpdate();

        if (updatedCount == 0) {
            throw new RuntimeException("User profile not found for user account ID: " + userAccountId);
        }
        } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid gender value. Allowed values are 'MALE' or 'FEMALE'.", e);
        }

        return getUserProfileByUserAccountId(userAccountId);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileResponse getUserProfileByUserAccountId(Long userAccountId) {
        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, full_name, email, gender FROM user_profiles WHERE user_account_id = ?")
                .setParameter(1, userAccountId)
                .getSingleResult();

        UserProfileResponse response = new UserProfileResponse();
        response.setId(((Number) result[0]).longValue());
        response.setFullName((String) result[1]);
        response.setEmail((String) result[2]);
        response.setGender(Gender.valueOf((String) result[3]));
        response.setUserAccountId(userAccountId);
        return response;
    }
}
