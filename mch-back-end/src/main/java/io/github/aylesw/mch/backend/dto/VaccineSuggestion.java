package io.github.aylesw.mch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineSuggestion {
    private Long childId;
    private String childName;
    private String vaccineName;
    private Integer doseNo;
    private String ageGroupName;
}
