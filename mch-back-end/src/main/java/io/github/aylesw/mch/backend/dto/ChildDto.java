package io.github.aylesw.mch.backend.dto;

import jakarta.persistence.Column;
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
public class ChildDto {
    private Long id;

    @NotEmpty
    private String fullName;

    @NotNull
    private Date dob;

    @NotEmpty
    private String sex;

    private String ethnicity;

    private String birthplace;

    private String insuranceId;
}
