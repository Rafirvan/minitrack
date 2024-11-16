package com.rafirvan.minitrack.controller;

import com.rafirvan.minitrack.dto.request.LoginRequest;
import com.rafirvan.minitrack.dto.request.UserRegistrationRequest;
import com.rafirvan.minitrack.dto.response.UserRegistrationResponse;
import com.rafirvan.minitrack.service.UserAccountService;
import com.rafirvan.minitrack.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Account Management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Operation(summary = "Register a new user account")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        UserRegistrationResponse response = userAccountService.registerUser(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "User registered successfully", response);
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        userAccountService.loginUser(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "User logged in successfully", null);
    }
}
