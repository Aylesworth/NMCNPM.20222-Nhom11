package io.github.aylesw.mch.backend.dto;

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

    @NotNull
    private Long vaccineId;

    private String vaccineName;

    private Integer vaccineDoseNo;

    @NotNull
    private Date date;

    private String note;

    private List<String> reactions;
}
