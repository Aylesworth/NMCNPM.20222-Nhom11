package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.BodyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyMetricsRepository extends JpaRepository<BodyMetrics, Long> {
    @Query("SELECT b FROM BodyMetrics b JOIN FETCH b.child c WHERE c.id = ?1")
    public List<BodyMetrics> findByChildId(Long childId);
}
