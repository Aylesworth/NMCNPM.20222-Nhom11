package io.github.aylesw.mch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverallStatistics {
    private long numberOfUsers;
    private long userIncreaseByLastMonth;
    private long numberOfChildren;
    private long childIncreaseByLastMonth;
    private long numberOfInjections;
    private long injectionIncreaseByLastMonth;
    private List<VaccineStatisticsDetails> vaccineStatistics;
    private List<InjectionStatisticsDetails> injectionStatistics;
}
