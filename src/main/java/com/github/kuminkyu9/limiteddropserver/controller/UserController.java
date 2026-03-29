package com.github.kuminkyu9.limiteddropserver.controller;

import com.github.kuminkyu9.limiteddropserver.dto.auth.LoginResponse;
import com.github.kuminkyu9.limiteddropserver.entity.User;
import com.github.kuminkyu9.limiteddropserver.entity.UserRole;
import com.github.kuminkyu9.limiteddropserver.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignupRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRole(request.getRole());

        userService.signup(user);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class UserSignupRequest {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @NotNull
        private UserRole role;
    }

    @Data
    public static class LoginRequest {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }
}