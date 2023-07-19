package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    @Query("""
            SELECT r FROM Role r
            JOIN FETCH r.users u
            WHERE u.id = ?1
            """)
    List<Role> getRolesOfUser(Long userId);
}
