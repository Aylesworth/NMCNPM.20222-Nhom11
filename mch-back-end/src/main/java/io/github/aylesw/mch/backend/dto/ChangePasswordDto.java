package io.github.aylesw.mch.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {
    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
