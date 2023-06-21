package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {
    @Query("SELECT n FROM SystemNotification n JOIN FETCH n.user u " +
            "WHERE u.id = ?1 AND n.time <= ?2" +
            "ORDER BY n.time DESC")
    List<SystemNotification> findByUserIdBeforeTime(Long userId, Timestamp time);

}
