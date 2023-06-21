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
public class VerifyEmailDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String authCode;
}
