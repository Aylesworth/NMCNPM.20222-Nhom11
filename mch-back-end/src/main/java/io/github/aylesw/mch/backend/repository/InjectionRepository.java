package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Injection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface InjectionRepository extends JpaRepository<Injection, Long> {
    @Query("SELECT i FROM Injection i JOIN FETCH i.child c WHERE c.id = ?1 ORDER BY i.date")
    List<Injection> findByChildId(Long childId);

    @Query("SELECT i FROM Injection i WHERE i.date >= ?1 AND i.status NOT LIKE ?2 ORDER BY i.date")
    List<Injection> findAfterDateWithStatusOtherThan(Date date, String status);

    @Query("SELECT i FROM Injection i WHERE i.status LIKE ?1 ORDER BY i.date")
    List<Injection> findByStatus(String status);
}
