package com.github.kuminkyu9.limiteddropserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트를 위해 CSRF 보안 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/users/signup", "/api/users/login").permitAll() // 회원가입은 누구나 허용
                        .anyRequest().authenticated() // 나머지는 로그인 필요
                );

        return http.build();
    }
}