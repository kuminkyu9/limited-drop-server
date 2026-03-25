package com.github.kuminkyu9.limiteddropserver.repository;

import com.github.kuminkyu9.limiteddropserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository를 상속받으면 기본적인 CRUD(저장, 조회, 수정, 삭제) 기능 가짐
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 찾는 기능을 추가 (중복 가입 확인이나 로그인 시 사용)
    Optional<User> findByEmail(String email);
}