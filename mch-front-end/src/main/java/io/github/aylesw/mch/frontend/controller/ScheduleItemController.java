package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.Beans;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class ScheduleItemController implements Initializable {

    private Map<String, Object> properties;

    public ScheduleItemController(Map<String, Object> properties) {
        this.properties = properties;
    }

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDayOfWeek;

    @FXML
    private Label lblChild;

    @FXML
    private Label lblVaccine;

    @FXML
    private Label lblDoseNo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblChild.setText(properties.get("childName").toString());
        lblVaccine.setText(properties.get("vaccineName").toString());
        lblDoseNo.setText(Utils.toLongValue(properties.get("vaccineDoseNo")) + "");

        LocalDate date = LocalDate.parse(properties.get("date").toString());
        lblDate.setText(date.format(Beans.DATE_FORMATTER));

        if (date.getDayOfWeek().getValue() != 7)
            lblDayOfWeek.setText("Thứ " + (date.getDayOfWeek().getValue() + 1));
        else lblDayOfWeek.setText("Chủ nhật");
    }
}

