package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.entity.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {
    @Query("SELECT n FROM SystemNotification n JOIN FETCH n.user u " +
            "WHERE u.id = ?1 AND n.time <= ?2 " +
            "ORDER BY n.time DESC")
    List<SystemNotification> findByUserIdBeforeTime(Long userId, Timestamp time);

    @Modifying
    @Transactional
    @Query("UPDATE SystemNotification n SET n.seen = true WHERE n.user.id = ?1 AND n.time <= ?2")
    void updateSeenByUserIdBeforeTime(Long userId, Timestamp time);

    @Modifying
    @Transactional
    @Query("DELETE FROM SystemNotification n WHERE n.title LIKE %?1% AND n.message LIKE %?2%")
    void deleteByTitleAndMessage(String title, String message);

    @Modifying
    @Transactional
    @Query("DELETE FROM SystemNotification n WHERE n.user.id = ?1")
    void deleteByUserId(Long userId);
}
