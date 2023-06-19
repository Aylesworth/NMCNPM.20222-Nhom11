package com.aylesw.mch.backend;

import com.aylesw.mch.backend.repository.RoleRepository;
import com.aylesw.mch.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class MaternalChildHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaternalChildHealthApplication.class, args);
    }

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner runner() {
        return (args -> {
//            roleRepository.save(new Role(null, "ROLE_ADMIN", Set.of()));
//            roleRepository.save(new Role(null, "ROLE_USER", Set.of()));

//            DangKyUser dangKyUser = DangKyUser.builder()
//                    .email("admin")
//                    .password("admin")
//                    .tenDayDu("Admin")
//                    .gioiTinh("NULL")
//                    .ngaySinh(Date.valueOf("1900-01-01"))
//                    .sdt("NULL")
//                    .diaChi("NULL")
//                    .build();

//            userService.createUser(dangKyUser);

            System.out.println(userService.search("Admin"));
        });
    }
}
