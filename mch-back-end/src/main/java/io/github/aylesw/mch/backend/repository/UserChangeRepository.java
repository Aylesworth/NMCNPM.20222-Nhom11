package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.UserChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChangeRepository extends JpaRepository<UserChange, Long> {
    @Query("SELECT c FROM UserChange c WHERE c.approved IS NULL")
    List<UserChange> findNotApproved();
}
