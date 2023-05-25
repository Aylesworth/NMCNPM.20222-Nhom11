package mch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mch.model.BodyMetrics;

@Repository
public interface BodyMetricsRepository extends JpaRepository<BodyMetrics, Long> {
	
	@Query("SELECT bm FROM BodyMetrics bm JOIN FETCH bm.child c WHERE c.id = ?1")
	public List<BodyMetrics> findByChild(Long childId);
	
	@Query("SELECT bm FROM BodyMetrics bm JOIN FETCH bm.child c WHERE c.id = ?1 AND bm.id = ?2")
	public Optional<BodyMetrics> findByChildAndId(Long childId, Long id);
}
