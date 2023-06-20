package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.UserChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChangeRepository extends JpaRepository<UserChange, Long> {
}
