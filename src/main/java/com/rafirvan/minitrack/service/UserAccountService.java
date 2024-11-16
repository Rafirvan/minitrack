package com.rafirvan.minitrack.service;

import com.rafirvan.minitrack.dto.request.LoginRequest;
import com.rafirvan.minitrack.dto.request.UserRegistrationRequest;
import com.rafirvan.minitrack.dto.response.UserRegistrationResponse;

public interface UserAccountService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    void loginUser(LoginRequest loginRequest);
}
