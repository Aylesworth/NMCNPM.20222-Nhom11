package io.github.aylesw.mch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineStatisticsItem implements Comparable<VaccineStatisticsItem> {
    private String vaccine;
    private Integer doseNo;
    private Long quantity;

    @Override
    public int compareTo(VaccineStatisticsItem other) {
        if (!this.vaccine.equals(other.vaccine))
            return this.vaccine.compareTo(other.vaccine);

        return this.doseNo - other.doseNo;
    }
}
