package com.github.kuminkyu9.limiteddropserver.controller;

import com.github.kuminkyu9.limiteddropserver.entity.User;
import com.github.kuminkyu9.limiteddropserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController // JSON 데이터를 주고받는 API 컨트롤러로 설정
@RequiredArgsConstructor
@RequestMapping("/api/users") // 이 컨트롤러의 모든 주소는 /api/users로 시작함
public class UserController {

    private final UserService userService;

    // 회원가입 API (POST 방식)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setName(request.getName());

            userService.signup(user);

            return ResponseEntity.ok("회원가입이 완료되었습니다. ID: " + user.getEmail());
        } catch (IllegalArgumentException e) {
            // 중복된 이메일 등의 예외 발생 시 400 Bad Request와 에러 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원가입 시 받을 데이터를 담는 바구니 (DTO)
    @Data
    public static class UserSignupRequest {
        private String email;
        private String password;
        private String name;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String[] tokens = userService.login(request.getEmail(), request.getPassword());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", tokens[0]);
        response.put("refreshToken", tokens[1]);

        return ResponseEntity.ok(response);
    }

    // 로그인 요청 데이터를 받을 바구니
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}