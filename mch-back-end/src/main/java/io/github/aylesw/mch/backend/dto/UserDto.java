package io.github.aylesw.mch.backend.dto;

import jakarta.validation.constraints.Email;
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
public class UserDto {
    private Long id;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String fullName;

    @NotNull
    private Date dob;

    private Long age;

    @NotEmpty
    private String sex;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String address;

    private String citizenId;

    private String insuranceId;
}
