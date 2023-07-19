package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.fromDate <= ?1 AND ?1 <= e.toDate ORDER BY e.fromDate")
    List<Event> findEventsAvailableOn(Date date);

    @Query("SELECT e FROM Event e JOIN FETCH e.participants u WHERE u.id = ?1 ORDER BY e.fromDate")
    List<Event> findByUserId(Long userId);
}
