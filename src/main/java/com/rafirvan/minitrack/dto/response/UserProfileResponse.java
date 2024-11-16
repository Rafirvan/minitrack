package com.rafirvan.minitrack.dto.response;

import com.rafirvan.minitrack.constant.Gender;
import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private Long userAccountId;
    private String fullName;
    private Gender gender;
    private String email;
}
