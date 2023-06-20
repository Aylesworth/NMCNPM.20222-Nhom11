package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.ChildChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildChangeRepository extends JpaRepository<ChildChange, Long> {
}
