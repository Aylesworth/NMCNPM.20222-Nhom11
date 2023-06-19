package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.ThayDoiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThayDoiUserRepository extends JpaRepository<ThayDoiUser, Long> {
}
