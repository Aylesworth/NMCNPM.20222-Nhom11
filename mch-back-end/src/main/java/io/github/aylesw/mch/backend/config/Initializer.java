package io.github.aylesw.mch.backend.config;

import io.github.aylesw.mch.backend.dto.RegisterDto;
import io.github.aylesw.mch.backend.dto.UserDto;
import io.github.aylesw.mch.backend.model.Role;
import io.github.aylesw.mch.backend.repository.*;
import io.github.aylesw.mch.backend.service.NotificationService;
import io.github.aylesw.mch.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ChildRepository childRepository;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final AgeGroupRepository ageGroupRepository;
    private final VaccineRepository vaccineRepository;

    @Override
    public void run(String... args) throws Exception {
//
//        notificationService.scheduleEmailsOnStart();
//
//            roleRepository.save(new Role(null, "ADMIN", Set.of()));
//            roleRepository.save(new Role(null, "USER", Set.of()));
//
//            RegisterDto registerDto = RegisterDto.builder()
//                    .email("admin")
//                    .password("admin")
//                    .fullName("Admin")
//                    .sex("null")
//                    .dob(Date.valueOf("1900-01-01"))
//                    .phoneNumber("null")
//                    .address("null")
//                    .build();
//
//            userService.createUser(registerDto);

//            System.out.println(userService.search("Admin"));

//        ageGroupRepository.saveAll(List.of(
//                AgeGroup.builder().id(0).name("Sơ sinh").minAgeInDays(0).maxAgeInDays(61).build(),
//                AgeGroup.builder().id(2).name("2 tháng tuổi").minAgeInDays(61).maxAgeInDays(91).build(),
//                AgeGroup.builder().id(3).name("3 tháng tuổi").minAgeInDays(91).maxAgeInDays(121).build(),
//                AgeGroup.builder().id(4).name("4 tháng tuổi").minAgeInDays(121).maxAgeInDays(181).build(),
//                AgeGroup.builder().id(6).name("6 tháng tuổi").minAgeInDays(181).maxAgeInDays(211).build(),
//                AgeGroup.builder().id(7).name("7 tháng tuổi").minAgeInDays(211).maxAgeInDays(241).build(),
//                AgeGroup.builder().id(8).name("8 tháng tuổi").minAgeInDays(241).maxAgeInDays(271).build(),
//                AgeGroup.builder().id(9).name("9 tháng tuổi").minAgeInDays(271).maxAgeInDays(301).build(),
//                AgeGroup.builder().id(10).name("10 tháng tuổi").minAgeInDays(301).maxAgeInDays(361).build(),
//                AgeGroup.builder().id(12).name("12 tháng tuổi").minAgeInDays(361).maxAgeInDays(451).build(),
//                AgeGroup.builder().id(15).name("15 tháng tuổi").minAgeInDays(451).maxAgeInDays(541).build(),
//                AgeGroup.builder().id(18).name("18 tháng tuổi").minAgeInDays(541).maxAgeInDays(730).build(),
//                AgeGroup.builder().id(24).name("2 đến 3 tuổi").minAgeInDays(730).maxAgeInDays(1460).build()
//        ));
//
//        vaccineRepository.saveAll(List.of(
//                Vaccine.builder().name("Lao").doseNo(1).ageGroup(ageGroupRepository.findById(0).get()).build(),
//                Vaccine.builder().name("Viêm gan B").doseNo(1).ageGroup(ageGroupRepository.findById(0).get()).build(),
//                Vaccine.builder().name("Viêm gan B").doseNo(2).ageGroup(ageGroupRepository.findById(2).get()).build(),
//                Vaccine.builder().name("Phế cầu").doseNo(1).ageGroup(ageGroupRepository.findById(2).get()).build(),
//                Vaccine.builder().name("Tiêu chảy do vi rút Rota").doseNo(1).ageGroup(ageGroupRepository.findById(2).get()).build(),
//                Vaccine.builder().name("Bạch cầu - ho gà - uốn ván - bại liệt - viêm gan B - HiB").doseNo(1).ageGroup(ageGroupRepository.findById(2).get()).build(),
//                Vaccine.builder().name("Viêm gan B").doseNo(3).ageGroup(ageGroupRepository.findById(3).get()).build(),
//                Vaccine.builder().name("Phế cầu").doseNo(2).ageGroup(ageGroupRepository.findById(3).get()).build(),
//                Vaccine.builder().name("Tiêu chảy do vi rút Rota").doseNo(2).ageGroup(ageGroupRepository.findById(3).get()).build(),
//                Vaccine.builder().name("Bạch cầu - ho gà - uốn ván - bại liệt - viêm gan B - HiB").doseNo(2).ageGroup(ageGroupRepository.findById(3).get()).build(),
//                Vaccine.builder().name("Phế cầu").doseNo(3).ageGroup(ageGroupRepository.findById(4).get()).build(),
//                Vaccine.builder().name("Bạch cầu - ho gà - uốn ván - bại liệt - viêm gan B - HiB").doseNo(3).ageGroup(ageGroupRepository.findById(4).get()).build(),
//                Vaccine.builder().name("Cúm").doseNo(1).ageGroup(ageGroupRepository.findById(6).get()).build(),
//                Vaccine.builder().name("Viêm màng não do mô cầu BC").doseNo(1).ageGroup(ageGroupRepository.findById(6).get()).build(),
//                Vaccine.builder().name("Cúm").doseNo(2).ageGroup(ageGroupRepository.findById(7).get()).build(),
//                Vaccine.builder().name("Viêm màng não do mô cầu BC").doseNo(2).ageGroup(ageGroupRepository.findById(8).get()).build(),
//                Vaccine.builder().name("Viêm màng não do mô cầu AC").doseNo(1).ageGroup(ageGroupRepository.findById(9).get()).build(),
//                Vaccine.builder().name("Sởi đơn").doseNo(1).ageGroup(ageGroupRepository.findById(9).get()).build(),
//                Vaccine.builder().name("Phế cầu").doseNo(4).ageGroup(ageGroupRepository.findById(10).get()).build(),
//                Vaccine.builder().name("Viêm não Nhật Bản B").doseNo(1).ageGroup(ageGroupRepository.findById(12).get()).build(),
//                Vaccine.builder().name("Viêm não Nhật Bản B").doseNo(2).ageGroup(ageGroupRepository.findById(12).get()).build(),
//                Vaccine.builder().name("Viêm gan A").doseNo(1).ageGroup(ageGroupRepository.findById(12).get()).build(),
//                Vaccine.builder().name("Viêm màng não do mô cầu AC").doseNo(2).ageGroup(ageGroupRepository.findById(12).get()).build(),
//                Vaccine.builder().name("Sởi - Quai bị - Rubella").doseNo(1).ageGroup(ageGroupRepository.findById(12).get()).build(),
//                Vaccine.builder().name("Thủy đậu").doseNo(1).ageGroup(ageGroupRepository.findById(15).get()).build(),
//                Vaccine.builder().name("Sởi - Quai bị - Rubella").doseNo(2).ageGroup(ageGroupRepository.findById(15).get()).build(),
//                Vaccine.builder().name("Viêm gan A").doseNo(2).ageGroup(ageGroupRepository.findById(18).get()).build(),
//                Vaccine.builder().name("Bạch cầu - ho gà - uốn ván - bại liệt - viêm gan B - HiB").doseNo(4).ageGroup(ageGroupRepository.findById(18).get()).build(),
//                Vaccine.builder().name("Sởi đơn").doseNo(2).ageGroup(ageGroupRepository.findById(18).get()).build(),
//                Vaccine.builder().name("Viêm gan B").doseNo(4).ageGroup(ageGroupRepository.findById(18).get()).build(),
//                Vaccine.builder().name("Viêm não Nhật Bản B").doseNo(3).ageGroup(ageGroupRepository.findById(24).get()).build(),
//                Vaccine.builder().name("Thương hàn").doseNo(1).ageGroup(ageGroupRepository.findById(24).get()).build(),
//                Vaccine.builder().name("Bệnh tả").doseNo(1).ageGroup(ageGroupRepository.findById(24).get()).build(),
//                Vaccine.builder().name("Bệnh tả").doseNo(2).ageGroup(ageGroupRepository.findById(24).get()).build()
//        ));

//        System.out.println(childRepository.findById(1L).get().getAgeInDays());

    }
}
