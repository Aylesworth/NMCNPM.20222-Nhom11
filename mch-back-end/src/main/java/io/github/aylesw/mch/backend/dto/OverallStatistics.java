package io.github.aylesw.mch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverallStatistics {
    private long userCount;
    private long userIncreaseByLastMonth;
    private long childCount;
    private long childIncreaseByLastMonth;
    private long injectionCount;
    private long injectionIncreaseByLastMonth;
    private List<VaccineStatisticsDetails> vaccineStatistics;
    private List<InjectionStatisticsDetails> injectionStatistics;
    private Map<Long,Long> ageStatistics;
}
