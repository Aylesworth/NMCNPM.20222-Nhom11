package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.NotificationDto;
import com.aylesw.mch.backend.exception.ResourceNotFoundException;
import com.aylesw.mch.backend.model.Notification;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.NotificationRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import com.aylesw.mch.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    public void createNotification(Long userId, NotificationDto notificationDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = mapper.map(notificationDto, Notification.class);
        notification.setId(null);
        notification.setUser(user);

        notificationRepository.save(notification);

        if (notification.getSendsEmail()) {
            scheduleEmail(notification);
        }
    }

    @Override
    public List<NotificationDto> loadNotifications(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notification -> mapper.map(notification, NotificationDto.class))
                .toList();
    }

    private void scheduleEmail(Notification notification) {
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), notification.getScheduledTime().toLocalDateTime());
        executorService.schedule(
                () -> {
                    sendEmail(notification);
                },
                delay,
                TimeUnit.MILLISECONDS
        );
    }

    private void sendEmail(Notification notification) {
        System.out.println("Email sent to "+notification.getUser().getEmail());
        notification.setEmailSent(true);
        notificationRepository.save(notification);
    }

    @Override
    public void scheduleEmailsOnStart() {
        notificationRepository.findNotificationsWithUnsentEmail().stream()
                .forEach(this::scheduleEmail);
    }

}
