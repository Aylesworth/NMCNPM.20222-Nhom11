package io.github.aylesw.mch.backend.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Utils {
    public static Timestamp currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
