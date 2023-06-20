package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.ChildRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRegistrationRepository extends JpaRepository<ChildRegistration, Long> {
}
