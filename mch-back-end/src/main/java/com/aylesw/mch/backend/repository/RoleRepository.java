package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Optional<Role> findByName(String name);
	
	@Query("""
			SELECT r FROM Role r
			JOIN FETCH r.users u
			WHERE u.id = ?1
			""")
	public List<Role> getRolesOfUser(Long userId);
}
