package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.dto.NotificationDetails;
import io.github.aylesw.mch.backend.exception.ApiException;
import io.github.aylesw.mch.backend.model.EmailNotification;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.model.SystemNotification;
import io.github.aylesw.mch.backend.model.User;
import io.github.aylesw.mch.backend.repository.EmailNotificationRepository;
import io.github.aylesw.mch.backend.repository.SystemNotificationRepository;
import io.github.aylesw.mch.backend.repository.UserRepository;
import io.github.aylesw.mch.backend.service.GoogleCalendarService;
import io.github.aylesw.mch.backend.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final GoogleCalendarService googleCalendarService;
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
        var notifications = systemNotificationRepository.findByUserIdBeforeTime(userId, Utils.currentTimestamp());
//        notifications.forEach(notification -> {
//            notification.setSeen(true);
//            systemNotificationRepository.save(notification);
//        });
        systemNotificationRepository.updateSeenByUserIdBeforeTime(userId, Utils.currentTimestamp());
        return notifications;
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

    @Override
    public void createNotificationsAboutInjection(Injection injection) {
        LocalDateTime eventDateTime = injection.getDate().toLocalDate().atTime(LocalTime.of(9, 0));

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .time(Utils.currentTimestamp())
                .title("Đăng ký tiêm chủng được tiếp nhận")
                .message("Lịch tiêm vaccine %s mũi số %d cho bé %s sẽ diễn ra vào ngày %s (thời gian tiếp nhận 9:00 - 17:00)"
                        .formatted(injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                injection.getChild().getFullName(),
                                injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .user(injection.getChild().getParent())
                .build();
        createSystemNotification(notificationDetails);

        notificationDetails.setTitle("Nhắc lịch tiêm chủng");
        notificationDetails.setMessage("Lịch tiêm vaccine %s mũi số %d cho bé %s sẽ diễn ra vào ngày mai %s (9:00 - 17:00)"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName(),
                        injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        notificationDetails.setTime(Timestamp.valueOf(eventDateTime.minusDays(1)));
        createSystemNotification(notificationDetails);
        createEmailNotification(notificationDetails);

        notificationDetails.setTitle("Nhắc lịch tiêm chủng");
        notificationDetails.setMessage("Lịch tiêm vaccine %s mũi số %d cho bé %s diễn ra vào hôm nay %s (9:00 - 17:00)"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName(),
                        injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        notificationDetails.setTime(Timestamp.valueOf(eventDateTime.minusHours(1)));
        createSystemNotification(notificationDetails);
        createEmailNotification(notificationDetails);
    }

    @Override
    public void deleteNotificationsAboutInjection(Injection injection) {
        String message = "vaccine %s mũi số %d cho bé %s"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName());
        systemNotificationRepository.deleteByTitleAndMessage("Nhắc lịch", message);
        emailNotificationRepository.deleteByMessage(message);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .time(Utils.currentTimestamp())
                .title("Lịch tiêm bị hủy")
                .message("Lịch tiêm vaccine %s mũi số %d cho bé %s tạm thời bị hủy"
                        .formatted(injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                injection.getChild().getFullName()))
                .user(injection.getChild().getParent())
                .build();
        createSystemNotification(notificationDetails);
    }

    @Override
    public void updateNotificationsAboutInjection(Injection injection) {
        String message = "vaccine %s mũi số %d cho bé %s"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName());
        systemNotificationRepository.deleteByTitleAndMessage("Nhắc lịch", message);
        emailNotificationRepository.deleteByMessage(message);

        LocalDateTime eventDateTime = injection.getDate().toLocalDate().atTime(LocalTime.of(9, 0));

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .time(Utils.currentTimestamp())
                .title("Thay đổi lịch tiêm")
                .message("Lịch tiêm vaccine %s mũi số %d cho bé %s đã được chuyển sang ngày %s (thời gian tiếp nhận 9:00 - 17:00)"
                        .formatted(injection.getVaccine().getName(),
                                injection.getVaccine().getDoseNo(),
                                injection.getChild().getFullName(),
                                injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .user(injection.getChild().getParent())
                .build();
        createSystemNotification(notificationDetails);

        notificationDetails.setTitle("Nhắc lịch tiêm chủng");
        notificationDetails.setMessage("Lịch tiêm vaccine %s mũi số %d cho bé %s sẽ diễn ra vào ngày mai %s (9:00 - 17:00)"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName(),
                        injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        notificationDetails.setTime(Timestamp.valueOf(eventDateTime.minusDays(1)));
        createSystemNotification(notificationDetails);
        createEmailNotification(notificationDetails);

        notificationDetails.setTitle("Nhắc lịch tiêm chủng");
        notificationDetails.setMessage("Lịch tiêm vaccine %s mũi số %d cho bé %s diễn ra vào hôm nay %s (9:00 - 17:00)"
                .formatted(injection.getVaccine().getName(),
                        injection.getVaccine().getDoseNo(),
                        injection.getChild().getFullName(),
                        injection.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        notificationDetails.setTime(Timestamp.valueOf(eventDateTime.minusHours(1)));
        createSystemNotification(notificationDetails);
        createEmailNotification(notificationDetails);
    }
}
