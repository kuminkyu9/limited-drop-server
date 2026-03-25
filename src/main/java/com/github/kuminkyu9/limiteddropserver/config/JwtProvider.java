package com.github.kuminkyu9.limiteddropserver.config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    // 실무에서는 이 키를 환경변수나 설정파일로 빼야 합니다.
    private final String secret = "your-very-secret-key-32-characters-long!!";
    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    private final long ACCESS_TIME = 1000L * 60 * 30; // 30분
    private final long REFRESH_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    public String createAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TIME))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .signWith(key)
                .compact();
    }
}