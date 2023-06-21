package com.aylesw.mch.backend.dto;

import com.aylesw.mch.backend.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
        this.time = time == null ? Timestamp.valueOf(LocalDateTime.now()) : time;
        this.user = user;
    }
}
