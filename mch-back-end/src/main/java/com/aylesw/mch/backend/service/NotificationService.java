package com.aylesw.mch.backend.service;

import com.aylesw.mch.backend.dto.NotificationDto;
import com.aylesw.mch.backend.model.Notification;

import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, NotificationDto notificationDto);

    List<NotificationDto> loadNotifications(Long userId);

    void scheduleEmailsOnStart();
}
