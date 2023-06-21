package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.NotificationDetails;
import com.aylesw.mch.backend.model.SystemNotification;

import java.util.List;

public interface NotificationService {
    void createSystemNotification(NotificationDetails notificationDetails);

    void createEmailNotification(NotificationDetails notificationDetails);

    List<SystemNotification> getSystemNotifications(Long userId);

    void scheduleEmailsOnStart();
}
