package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    @Query("SELECT c FROM AuthCode c WHERE c.value = ?1 AND c.expiresAt > ?2")
    Optional<AuthCode> findByValueNonExpiredAtTime(String value, Timestamp time);
}
