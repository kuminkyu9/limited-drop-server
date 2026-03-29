package com.github.kuminkyu9.limiteddropserver.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private Long userId;
    private String email;
}