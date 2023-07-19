package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.fullName LIKE %?1% OR u.email LIKE %?1%")
	List<User> findByKeyword(String keyword);

	boolean existsByEmail(String email);

	@Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.name LIKE 'ADMIN'")
	List<User> findAdmins();

}
