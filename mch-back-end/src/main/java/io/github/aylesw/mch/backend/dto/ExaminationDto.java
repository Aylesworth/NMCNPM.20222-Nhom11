package io.github.aylesw.mch.backend.dto;

import io.github.aylesw.mch.backend.model.Medicine;
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
public class ExaminationDto {
    private Long id;

    @NotNull
    private Date date;

    @NotEmpty
    private String facility;

    @NotEmpty
    private String reason;

    @NotEmpty
    private String result;

    private String note;

    private List<Medicine> medicines;
}
