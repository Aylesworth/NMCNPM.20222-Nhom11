package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.ChildRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRegistrationRepository extends JpaRepository<ChildRegistration, Long> {
    @Query("SELECT r FROM ChildRegistration r WHERE r.approved IS NULL")
    List<ChildRegistration> findNotApproved();
}
