package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    @Query("SELECT e FROM Examination e JOIN FETCH e.child c WHERE c.id = ?1")
    List<Examination> findByChildId(Long childId);
}
