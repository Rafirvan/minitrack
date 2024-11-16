package com.rafirvan.minitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
