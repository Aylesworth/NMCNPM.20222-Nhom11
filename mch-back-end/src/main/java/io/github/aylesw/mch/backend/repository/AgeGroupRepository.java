package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Integer> {
}
