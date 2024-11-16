package com.rafirvan.minitrack.dto.response;


import lombok.*;

@Data
public class UserAccountResponse {
    private Long id;
    private String username;
    private String fullName;
    private String gender;
    private String email;
    private String role;
}
