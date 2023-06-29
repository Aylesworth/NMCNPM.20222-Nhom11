package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.Beans;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class NotificationItemController implements Initializable {

    private String title;
    private String message;
    private LocalDateTime time;
    private boolean seen;

    NotificationItemController(Map<String, Object> properties) {
        title = properties.get("title").toString();
        message = properties.get("message").toString();
        time = LocalDateTime.parse(properties.get("time").toString(), Beans.JSON_TIME_FORMATTER).plusHours(7);
        seen = (Boolean) properties.get("seen");
    }

    @FXML
    private VBox container;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTitle.setText(title);
        lblMessage.setText(message);
        lblTime.setText(calculateTimeDifference());
        container.setStyle("-fx-background-color: " + (seen ? "#f5a105" : "#ffc559") + "; -fx-background-radius: 20");
    }

    private String calculateTimeDifference() {
        final long MINUTE = 60;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;

        LocalDateTime now = LocalDateTime.now();
        long seconds = Duration.between(time, now).getSeconds();
        if (seconds / MINUTE == 0) {
            return seconds + " giây trước";
        }
        if (seconds / HOUR == 0) {
            return (seconds % HOUR / MINUTE) + " phút trước";
        }
        if (seconds / DAY == 0) {
            return (seconds % DAY / HOUR) + " giờ trước";
        }

        YearMonth start = YearMonth.from(now);
        YearMonth end = YearMonth.from(time);

        long days = Duration.between(time, now).toDays();
        long months = start.until(end, ChronoUnit.MONTHS);
        long years = start.until(end, ChronoUnit.YEARS);
        if (months == 0) {
            return days + " ngày trước";
        }
        if (years == 0) {
            return months + " tháng trước";
        }
        return years + " năm trước";
    }
}
