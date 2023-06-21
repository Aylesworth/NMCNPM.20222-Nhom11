package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.fromDate <= ?1 AND ?1 <= e.toDate")
    List<Event> findEventsAvailableOn(Date date);
}
