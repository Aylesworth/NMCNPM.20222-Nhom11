package io.github.aylesw.mch.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InjectionDto {
    private Long id;

    private Long childId;

    private String childName;

    private Date childDob;

    private Long childAgeInMonths;

    private Long vaccineId;

    @NotEmpty
    private String vaccineName;

    @NotNull
    private Integer vaccineDoseNo;

    @NotNull
    private Date date;

    private String note;

    @NotEmpty
    private String status;

    private List<String> reactions;
}
