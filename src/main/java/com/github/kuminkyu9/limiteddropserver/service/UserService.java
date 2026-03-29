package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.config.JwtTokenProvider;
import com.github.kuminkyu9.limiteddropserver.dto.auth.LoginResponse;
import com.github.kuminkyu9.limiteddropserver.entity.User;
import com.github.kuminkyu9.limiteddropserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public Long signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("회원 유형은 CUSTOMER 또는 SELLER 중 하나여야 합니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        Date refreshTokenExpiration = jwtTokenProvider.getExpiration(refreshToken);
        LocalDateTime expiryAt = Instant.ofEpochMilli(refreshTokenExpiration.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        refreshTokenService.saveRefreshToken(user, refreshToken, expiryAt);

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                accessToken,
                refreshToken
        );
    }
}