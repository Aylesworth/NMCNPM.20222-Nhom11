package com.aylesw.mch.backend.config;

import com.aylesw.mch.backend.dto.NotificationDetails;
import com.aylesw.mch.backend.model.User;
import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

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
//        notificationService.createEmailNotification(
//                NotificationDetails.builder()
//                        .title("Subject")
//                        .message("This is a message")
//                        .user(User.builder().email("4985ghisusrh938qg83hoetu@gmail.com").build())
//                        .time(Timestamp.valueOf(LocalDateTime.of(2023, 6, 21, 13, 51)))
//                        .build()
//        );

        notificationService.scheduleEmailsOnStart();
//
//        bodyMetricsService.requestUpdate(1L);

//        authenticationService.requestPasswordReset("nguyenducanh2105@gmail.com");

    }
}
