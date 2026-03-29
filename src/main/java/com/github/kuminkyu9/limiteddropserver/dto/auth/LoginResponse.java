package com.github.kuminkyu9.limiteddropserver.dto.auth;

import com.github.kuminkyu9.limiteddropserver.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String accessToken;
    private String refreshToken;
}
