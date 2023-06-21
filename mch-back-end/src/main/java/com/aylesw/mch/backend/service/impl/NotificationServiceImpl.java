package com.aylesw.mch.backend.service.impl;

import com.aylesw.mch.backend.dto.NotificationDetails;
import com.aylesw.mch.backend.exception.ApiException;
import com.aylesw.mch.backend.model.EmailNotification;
import com.aylesw.mch.backend.model.SystemNotification;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.EmailNotificationRepository;
import com.aylesw.mch.backend.repository.SystemNotificationRepository;
import com.aylesw.mch.backend.repository.UserRepository;
import com.aylesw.mch.backend.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private final SystemNotificationRepository systemNotificationRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JavaMailSender mailSender;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    public void createSystemNotification(NotificationDetails notificationDetails) {
        User user = userRepository.findByEmail(notificationDetails.getUser().getEmail()).orElseThrow();

        SystemNotification systemNotification = mapper.map(notificationDetails, SystemNotification.class);
        systemNotification.setUser(user);
        systemNotification.setSeen(false);

        systemNotificationRepository.save(systemNotification);
    }

    @Override
    public void createEmailNotification(NotificationDetails notificationDetails) {
        EmailNotification emailNotification = mapper.map(notificationDetails, EmailNotification.class);
        emailNotification.setEmail(notificationDetails.getUser().getEmail());
        emailNotification.setSent(false);

        emailNotification = emailNotificationRepository.save(emailNotification);
        scheduleEmail(emailNotification);
    }

    @Override
    public List<SystemNotification> getSystemNotifications(Long userId) {
        return systemNotificationRepository.findByUserIdBeforeTime(userId, Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public void scheduleEmailsOnStart() {
        emailNotificationRepository.findAllUnsent()
                .forEach(this::scheduleEmail);
    }

    private void scheduleEmail(EmailNotification emailNotification) {
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), emailNotification.getTime().toLocalDateTime());
        executorService.schedule(
                () -> {
                    sendEmail(emailNotification);
                },
                delay,
                TimeUnit.MILLISECONDS
        );
    }

    private void sendEmail(EmailNotification emailNotification) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailNotification.getEmail());
            helper.setSubject(emailNotification.getTitle());
            helper.setText(emailNotification.getMessage(), true);

            mailSender.send(message);

            System.out.println("Mail sent to " + emailNotification.getEmail());

            emailNotification.setSent(true);
            emailNotificationRepository.save(emailNotification);
        } catch (MessagingException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
