package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Injection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionRepository extends JpaRepository<Injection, Long> {
    @Query("SELECT i FROM Injection i JOIN FETCH i.child c WHERE c.id = ?1")
    List<Injection> findByChildId(Long childId);
}
