package com.aylesw.mch.backend.repository;

import com.aylesw.mch.backend.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    @Query("SELECT n FROM EmailNotification n WHERE n.sent = false")
    List<EmailNotification> findAllUnsent();
}
