package com.rafirvan.minitrack.dto.response;


import lombok.*;

@Data
public class UserRegistrationResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String gender;
    private String email;
}