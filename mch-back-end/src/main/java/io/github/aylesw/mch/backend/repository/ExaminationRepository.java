package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    @Query("SELECT e FROM Examination e JOIN FETCH e.child c WHERE c.id = ?1 ORDER BY e.date")
    List<Examination> findByChildId(Long childId);
}
