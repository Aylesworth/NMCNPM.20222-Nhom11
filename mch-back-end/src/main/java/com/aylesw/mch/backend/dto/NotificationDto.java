package com.aylesw.mch.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class NotificationDto {
    private Long id;

    @NotEmpty
    private String message;

    private Timestamp scheduledTime;

    private Boolean sendsEmail;

    private Boolean emailSent;

    private Boolean seen;

    public NotificationDto(Long id, String message, Timestamp scheduledTime, Boolean sendsEmail, Boolean emailSent, Boolean seen) {
        this.id = id;
        this.message = message;
        this.scheduledTime = scheduledTime == null ? Timestamp.valueOf(LocalDateTime.now()) : scheduledTime;
        this.sendsEmail = sendsEmail == null ? false : sendsEmail;
        this.emailSent = emailSent == null ? (this.sendsEmail ? false : null) : emailSent;
        this.seen = seen == null ? false : seen;
    }
}
