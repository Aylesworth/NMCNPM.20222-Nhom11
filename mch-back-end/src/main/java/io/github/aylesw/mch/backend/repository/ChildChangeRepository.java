package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.ChildChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildChangeRepository extends JpaRepository<ChildChange, Long> {
    @Query("SELECT c FROM ChildChange c WHERE c.approved IS NULL")
    List<ChildChange> findNotApproved();
}
