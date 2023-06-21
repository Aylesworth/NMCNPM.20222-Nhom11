package com.aylesw.mch.backend.dto;

import jakarta.validation.constraints.NotEmpty;
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
public class EventDto {
    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    private Integer minAge;

    @NotNull
    private Integer maxAge;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;
}
