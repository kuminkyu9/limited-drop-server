package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.entity.RefreshToken;
import com.github.kuminkyu9.limiteddropserver.entity.User;
import com.github.kuminkyu9.limiteddropserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(User user, String token, LocalDateTime expiryAt) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(
                        RefreshToken.builder()
                                .user(user)
                                .revoked(false)
                                .build()
                );

        refreshToken.setToken(token);
        refreshToken.setExpiryAt(expiryAt);
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
    }
}