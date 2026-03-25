package com.github.kuminkyu9.limiteddropserver.service;

import com.github.kuminkyu9.limiteddropserver.config.JwtProvider;
import com.github.kuminkyu9.limiteddropserver.entity.RefreshToken;
import com.github.kuminkyu9.limiteddropserver.entity.User;
import com.github.kuminkyu9.limiteddropserver.repository.RefreshTokenRepository;
import com.github.kuminkyu9.limiteddropserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Repository를 자동으로 연결(주입)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    // 회원가입
    @Transactional // 데이터 저장 중 에러시 Rollback
    public Long signup(User user) {
        // 1. 중복 회원 검증 (이미 가입된 이메일인지 확인)
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2. DB에 저장
        userRepository.save(user);

        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    // 로그인
    @Transactional
    public String[] login(String email, String password) {
        // 1. 유저 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 확인 (나중에 암호화 적용 예정!)
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // 4. 7일 뒤 만료 설정
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);

        // 5. Refresh Token DB 저장 (기존 토큰 삭제 후 저장하면 더 깔끔)
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.save(new RefreshToken(refreshToken, user, expiryDate));

        return new String[]{accessToken, refreshToken};
    }
}