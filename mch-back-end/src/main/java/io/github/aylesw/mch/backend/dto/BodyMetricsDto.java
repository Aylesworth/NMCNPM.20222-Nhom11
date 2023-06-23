package io.github.aylesw.mch.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyMetricsDto {
    private Long id;

    @NotNull
    @Min(value = 0)
    private Double height;

    @NotNull
    @Min(value = 0)
    private Double weight;

    @NotNull
    private Date date;

    private String note;
}
