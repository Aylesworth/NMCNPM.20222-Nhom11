package com.aylesw.mch.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordDto {
    @NotEmpty
    private String authCode;

    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String newPassword;
}
