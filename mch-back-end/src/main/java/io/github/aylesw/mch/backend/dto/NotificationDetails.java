package io.github.aylesw.mch.backend.dto;

import io.github.aylesw.mch.backend.common.Utils;
import io.github.aylesw.mch.backend.model.Injection;
import io.github.aylesw.mch.backend.model.User;
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
        this.time = time == null ? Utils.currentTimestamp() : time;
        this.user = user;
    }
}
