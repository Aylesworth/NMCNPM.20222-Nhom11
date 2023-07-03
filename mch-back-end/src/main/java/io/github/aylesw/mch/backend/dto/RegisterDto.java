package io.github.aylesw.mch.backend.dto;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "[0-9]{10,12}")
    private String phoneNumber;

    @NotEmpty
    private String address;

    @Pattern(regexp = "[0-9]{12}")
    private String citizenId;

    @Pattern(regexp = "[A-Z0-9]{15}")
    private String insuranceId;
}
