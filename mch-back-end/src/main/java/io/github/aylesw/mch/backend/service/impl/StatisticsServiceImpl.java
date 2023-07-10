package io.github.aylesw.mch.backend.service.impl;

import io.github.aylesw.mch.backend.dto.InjectionStatisticsDetails;
import io.github.aylesw.mch.backend.dto.OverallStatistics;
import io.github.aylesw.mch.backend.dto.VaccineStatisticsDetails;
import io.github.aylesw.mch.backend.repository.*;
import io.github.aylesw.mch.backend.service.InjectionService;
import io.github.aylesw.mch.backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UserRepository userRepository;
    private final ChildRepository childRepository;
    private final InjectionRepository injectionRepository;
    private final InjectionService injectionService;

    @Override
    public OverallStatistics getOverallStatistics() {
        LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        long numberOfUsers = userRepository.count();
        long numberOfUsersByLastMonth = userRepository.findAll().stream()
                .filter(u -> u.getCreated().toLocalDateTime().toLocalDate().isBefore(firstDayOfMonth))
                .count();
        long userIncreaseByLastMonth = numberOfUsers - numberOfUsersByLastMonth;

        long numberOfChildren = childRepository.count();
        long numberOfChildrenByLastMonth = childRepository.findAll().stream()
                .filter(c -> c.getCreated().toLocalDateTime().toLocalDate().isBefore(firstDayOfMonth))
                .count();
        long childIncreaseByLastMonth = numberOfChildren - numberOfChildrenByLastMonth;

        long numberOfInjections = injectionRepository.findAll().stream()
                .filter(i -> !i.getDate().toLocalDate().isAfter(LocalDate.now()))
                .count();
        long numberOfInjectionsByLastMonth = injectionRepository.findAll().stream()
                .filter(i -> i.getDate().toLocalDate().isBefore(firstDayOfMonth))
                .count();
        long injectionIncreaseByLastMonth = numberOfInjections - numberOfInjectionsByLastMonth;

        List<VaccineStatisticsDetails> vaccineStatistics = injectionService.getVaccineStatistics(null, null).stream()
                .filter(v -> v.getDoseNo().equals(0))
                .toList();
        List<InjectionStatisticsDetails> injectionStatistics = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            long count = injectionRepository.countByDate(Date.valueOf(date));
            injectionStatistics.add(InjectionStatisticsDetails.builder().date(date).count(count).build());
        }

        return OverallStatistics.builder()
                .numberOfUsers(numberOfUsers)
                .userIncreaseByLastMonth(userIncreaseByLastMonth)
                .numberOfChildren(numberOfChildren)
                .childIncreaseByLastMonth(childIncreaseByLastMonth)
                .numberOfInjections(numberOfInjections)
                .injectionIncreaseByLastMonth(injectionIncreaseByLastMonth)
                .vaccineStatistics(vaccineStatistics)
                .injectionStatistics(injectionStatistics)
                .build();
    }
}
