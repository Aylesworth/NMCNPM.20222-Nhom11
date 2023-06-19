package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.DangKyUser;
import com.aylesw.mch.backend.model.ThayDoiUser;
import com.aylesw.mch.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.tenDayDu LIKE %?1% OR u.email LIKE %?1%")
	public List<User> find(String keyword);

	@Query("SELECT dk FROM DangKyUser dk WHERE dk.id = ?1")
	public Optional<DangKyUser> findDangKyUser(Long dangKyUserId);

	@Query("SELECT td FROM ThayDoiUser td WHERE td.id = ?1")
	public Optional<ThayDoiUser> findThayDoiUser(Long thayDoiUserId);
}
