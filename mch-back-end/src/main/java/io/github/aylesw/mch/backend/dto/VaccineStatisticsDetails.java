package io.github.aylesw.mch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineStatisticsDetails implements Comparable<VaccineStatisticsDetails> {
    private String vaccine;
    private Integer doseNo;
    private Long quantity;

    @Override
    public int compareTo(VaccineStatisticsDetails other) {
        if (!this.vaccine.equals(other.vaccine))
            return this.vaccine.compareTo(other.vaccine);

        return this.doseNo - other.doseNo;
    }
}
