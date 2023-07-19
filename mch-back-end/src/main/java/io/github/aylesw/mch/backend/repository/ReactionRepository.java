package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query("SELECT r FROM Reaction r JOIN FETCH r.injection i WHERE i.id = ?1")
    List<Reaction> findByInjectionId(Long injectionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.injection.id = ?1 AND r.details LIKE ?2")
    void deleteByInjectionIdAndDetails(Long injectionId, String details);
}
