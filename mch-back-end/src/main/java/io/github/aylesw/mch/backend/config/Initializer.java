package io.github.aylesw.mch.backend.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.aylesw.mch.backend.model.*;
import io.github.aylesw.mch.backend.repository.*;
import io.github.aylesw.mch.backend.service.NotificationService;
import io.github.aylesw.mch.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

//        insertRandomUsers();
//        insertRandomChildren();
//        insertRandomBodyMetrics();
//        insertRandomInjections();
//        insertRandomExaminations();
//        insertRandomEvents();
    }

    private final PasswordEncoder passwordEncoder;

    private Random rd = new Random();

    void insertRandomUsers() {
        var userData = apiGet("https://random-data-api.com/api/v2/users?size=100");
        userData.stream().forEach(data -> {
            String email = data.get("email").toString();
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(email.substring(0, email.indexOf('@'))))
                    .fullName(data.get("first_name").toString() + " " + data.get("last_name"))
                    .dob(Date.valueOf(generateRandomDate(LocalDate.now().minusYears(40), LocalDate.now().minusYears(16))))
                    .sex(rd.nextBoolean() ? "Nam" : "Nữ")
                    .phoneNumber("0" + generateNumberSeries(9))
                    .address(((Map<String, Object>) data.get("address")).get("street_address").toString())
                    .citizenId(generateNumberSeries(12))
                    .insuranceId(generateLetterSeries(2) + generateNumberSeries(13))
                    .verified(false)
                    .created(Timestamp.valueOf(generateRandomTime(LocalDateTime.now().minusMonths(6), LocalDateTime.now())))
                    .build();
            user = userRepository.save(user);
            System.out.println(user);
        });
    }

    void insertRandomChildren() {
        var childData = apiGet("https://random-data-api.com/api/v2/users?size=75");
        childData.stream().forEach(data -> {
            Child child = Child.builder()
                    .fullName(data.get("first_name").toString() + " " + data.get("last_name"))
                    .dob(Date.valueOf(generateRandomDate(LocalDate.now().minusYears(5), LocalDate.now())))
                    .sex(rd.nextBoolean() ? "Nam" : "Nữ")
                    .insuranceId(generateLetterSeries(2) + generateNumberSeries(13))
                    .ethnicity(rd.nextBoolean() ? "Kinh" : null)
                    .birthplace(((Map<String, Object>) data.get("address")).get("city").toString())
                    .created(Timestamp.valueOf(generateRandomTime(LocalDateTime.now().minusMonths(6), LocalDateTime.now())))
                    .parent(userRepository.findById(Long.valueOf(rd.nextInt(5, 108))).get())
                    .build();
            child = childRepository.save(child);
            System.out.println(child);
        });
    }

    private final BodyMetricsRepository bodyMetricsRepository;

    private void insertRandomBodyMetrics() {
        childRepository.findAll().forEach(child -> {
            int n = rd.nextInt(5, 15);
            LocalDate date = LocalDate.now().minusMonths(rd.nextInt(3, 6));
            double height = rd.nextDouble(50, 120);
            double weight = rd.nextDouble(5, 15);
            for (int i = 0; i < n; i++) {
                BodyMetrics bodyMetrics = BodyMetrics.builder()
                        .child(child)
                        .height(height = height + rd.nextDouble(0, 2))
                        .weight(weight = weight + rd.nextDouble(0, 1))
                        .date(Date.valueOf(date = date.plusDays(rd.nextInt(7, 14))))
                        .build();
                bodyMetricsRepository.save(bodyMetrics);
            }
        });
    }

    private final InjectionRepository injectionRepository;

    private void insertRandomInjections() {
        childRepository.findAll().forEach(child -> {
            int n = rd.nextInt(5, 11);
            for (int i = 0; i < n; i++) {
                Injection injection = Injection.builder()
                        .child(child)
                        .vaccine(vaccineRepository.findById(rd.nextLong(1, 35)).get())
                        .date(Date.valueOf(generateRandomDate(LocalDate.now().minusMonths(4), LocalDate.now().plusMonths(2))))
                        .status("Đã xác nhận")
                        .build();
                injectionRepository.save(injection);
            }
        });
    }

    private final ExaminationRepository examinationRepository;

    private void insertRandomExaminations() {
        childRepository.findAll().forEach(child -> {
            int n = rd.nextInt(5, 11);
            for (int i = 0; i < n; i++) {
                Examination examination = Examination.builder()
                        .date(Date.valueOf(generateRandomDate(LocalDate.now().minusMonths(4), LocalDate.now())))
                        .facility(generateRandomWords(rd.nextInt(1, 4)))
                        .reason(generateRandomWords(rd.nextInt(2, 6)))
                        .result(generateRandomWords(rd.nextInt(2, 6)))
                        .child(child)
                        .build();
                examinationRepository.save(examination);
            }
        });
    }

    private final EventRepository eventRepository;

    private void insertRandomEvents() {
        for (int i = 0; i < 10; i++) {
            int minAge = rd.nextInt(16, 40);
            int maxAge = 0;
            do {
                maxAge = rd.nextInt(16, 40);
            } while (maxAge <= minAge);
            LocalDate startDate = generateRandomDate(LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(1));
            LocalDate endDate = null;
            do {
                endDate = generateRandomDate(LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(2));
            } while (!endDate.isAfter(startDate));

            Event event = Event.builder()
                    .name("Chăm sóc sức khỏe phụ nữ từ %d đến %d tuổi".formatted(minAge, maxAge))
                    .minAge(minAge)
                    .maxAge(maxAge)
                    .fromDate(Date.valueOf(startDate))
                    .toDate(Date.valueOf(endDate))
                    .build();

            List<User> participants = new ArrayList<>();
            int n = rd.nextInt(1, 30);
            for (int j = 0; j < n; j++) {
                User user = null;
                do {
                    user = userRepository.findAll().get(rd.nextInt(0, (int) userRepository.count()));
                } while (user.getAge() > maxAge || user.getAge() < minAge || participants.contains(user));
                participants.add(user);
            }

            event.setParticipants(participants);
            eventRepository.save(event);
        }
    }

    private String generateNumberSeries(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rd.nextInt(0, 10));
        }
        return sb.toString();
    }

    private String generateLetterSeries(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) rd.nextInt('A', 'Z'));
        }
        return sb.toString();
    }

    private List<Map<String, Object>> apiGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<List<Map<String, Object>>>() {
        }.getType();

        return new Gson().fromJson(responseBody, type);
    }

    private <T> T apiGet(String url, Class<T> clazz) {
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<T>() {
        }.getType();

        return new Gson().fromJson(responseBody, type);
    }

    private LocalDate generateRandomDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    private LocalDateTime generateRandomTime(LocalDateTime startTime, LocalDateTime endTime) {
        long startEpochSecond = startTime.toEpochSecond(ZoneOffset.UTC);
        long endEpochSecond = endTime.toEpochSecond(ZoneOffset.UTC);
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
    }

    private String generateRandomWords(int numOfWords) {
        var words = apiGet("https://random-word-api.herokuapp.com/word?lang=en&number=" + numOfWords, new ArrayList<String>().getClass());
        return String.join(" ", words);
    }

}
