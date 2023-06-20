package com.aylesw.mch.backend.config;

import com.aylesw.mch.backend.dto.NotificationDto;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.service.BodyMetricsService;
import com.aylesw.mch.backend.service.NotificationService;
import com.aylesw.mch.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final BodyMetricsService bodyMetricsService;

    @Override
    public void run(String... args) throws Exception {
//            roleRepository.save(new Role(null, "ADMIN", Set.of()));
//            roleRepository.save(new Role(null, "USER", Set.of()));

//            UserDto userDto = UserDto.builder()
//                    .email("admin")
//                    .password("admin")
//                    .fullName("Admin")
//                    .sex("null")
//                    .dob(Date.valueOf("1900-01-01"))
//                    .phoneNumber("null")
//                    .address("null")
//                    .build();
//
//            userService.createUser(userDto);

//            System.out.println(userService.search("Admin"));
//
//        notificationService.createNotification(
//                5L,
//                NotificationDto.builder()
//                        .message("This is a message")
//                        .sendsEmail(true)
//                        .scheduledTime(Timestamp.valueOf(LocalDateTime.of(2023, 6, 20, 21, 24)))
//                        .build()
//        );
//
//        notificationService.scheduleEmailsOnStart();

//        bodyMetricsService.requestUpdate(1L);
    }
}
