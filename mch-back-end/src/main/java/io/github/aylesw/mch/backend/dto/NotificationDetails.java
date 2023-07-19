package io.github.aylesw.mch.backend.dto;

import io.github.aylesw.mch.backend.config.DateTimeUtils;
import io.github.aylesw.mch.backend.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Builder
public class NotificationDetails {
    @NotEmpty
    private String title;

    @NotEmpty
    private String message;

    private Timestamp time;

    private User user;

    public NotificationDetails(String title, String message, Timestamp time, User user) {
        this.title = title;
        this.message = message;
        this.time = time == null ? DateTimeUtils.currentTimestamp() : time;
        this.user = user;
    }
}
