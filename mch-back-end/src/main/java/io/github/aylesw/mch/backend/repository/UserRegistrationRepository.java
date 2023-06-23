package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {
    @Query("SELECT r FROM UserRegistration r WHERE r.approved IS NULL")
    List<UserRegistration> findNotApproved();
}
