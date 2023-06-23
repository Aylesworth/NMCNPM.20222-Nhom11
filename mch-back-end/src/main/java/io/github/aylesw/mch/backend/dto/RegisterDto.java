package io.github.aylesw.mch.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    private Long id;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotEmpty
    private String fullName;

    @NotNull
    private Date dob;

    @NotEmpty
    private String sex;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String address;

    private String citizenId;

    private String insuranceId;
}
