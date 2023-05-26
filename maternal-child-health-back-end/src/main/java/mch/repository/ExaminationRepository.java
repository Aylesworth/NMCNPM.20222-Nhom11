package mch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mch.model.Examination;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
	
	@Query("SELECT e FROM Examination e JOIN FETCH e.child c WHERE c.id = ?1")
	public List<Examination> findByChild(Long childId);
	
	@Query("SELECT e FROM Examination e JOIN FETCH e.child c WHERE c.id = ?1 AND e.id = ?2")
	public Optional<Examination> findByChildAndId(Long childId, Long id);
	
}
