package com.donggram.back.repository;

import com.donggram.back.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByStudentId(String studentId);
    Optional<RefreshToken> findByRefreshToken(String token);
}
