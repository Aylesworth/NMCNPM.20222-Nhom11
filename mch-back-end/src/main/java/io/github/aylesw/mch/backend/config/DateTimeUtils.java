package io.github.aylesw.mch.backend.config;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtils {
    public static Timestamp currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static Date currentDate() {
        return Date.valueOf(LocalDate.now());
    }
}
