package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n JOIN FETCH n.user u WHERE u.id = ?1 ORDER BY n.scheduledTime DESC")
    List<Notification> findByUserId(Long userId);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.sendsEmail = true " +
            "AND n.emailSent = false")
    List<Notification> findNotificationsWithUnsentEmail();
}
