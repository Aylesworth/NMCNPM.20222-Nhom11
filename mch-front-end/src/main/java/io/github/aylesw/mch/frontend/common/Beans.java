package io.github.aylesw.mch.frontend.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Beans {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final Gson GSON = new GsonBuilder().create();

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    public static final DateTimeFormatter JSON_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static final DateFormatConverter DATE_FORMAT_CONVERTER = new DateFormatConverter();

    public static final TimeFormatConverter TIME_FORMAT_CONVERTER = new TimeFormatConverter();

    public static final LocalDateStringConverter DATE_STRING_CONVERTER = new LocalDateStringConverter(DATE_FORMATTER, null);

    public static class DateFormatConverter {
        public String toCustom(String isoFormat) {
            return LocalDate.parse(isoFormat).format(DATE_FORMATTER);
        }

        public String toISO(String customFormat) {
            return LocalDate.parse(customFormat, DATE_FORMATTER).format(DateTimeFormatter.ISO_DATE);
        }
    }

    public static class TimeFormatConverter {
        public String toCustom(String isoFormat) {
            return LocalDateTime.parse(isoFormat, JSON_TIME_FORMATTER).format(TIME_FORMATTER);
        }

        public String toISO(String customFormat) {
            return LocalDateTime.parse(customFormat, TIME_FORMATTER).format(JSON_TIME_FORMATTER);
        }
    }
}
