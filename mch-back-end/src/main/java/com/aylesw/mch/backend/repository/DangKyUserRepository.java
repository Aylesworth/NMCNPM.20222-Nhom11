package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.DangKyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangKyUserRepository extends JpaRepository<DangKyUser, Long> {
}
