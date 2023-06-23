package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query("SELECT r FROM Reaction r JOIN FETCH r.injection i WHERE i.id = ?1")
    List<Reaction> findByInjectionId(Long injectionId);
}
