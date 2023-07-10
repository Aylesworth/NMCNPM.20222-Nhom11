package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.Beans;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GrowthChartsController implements Initializable {

    private List<Map<String, Object>> bodyMetricsData;

    public GrowthChartsController(List<Map<String, Object>> bodyMetricsData) {
        this.bodyMetricsData = bodyMetricsData;
    }

    @FXML
    private LineChart<String, Number> heightChart;

    @FXML
    private LineChart<String, Number> weightChart;

    @FXML
    private LineChart<String, Number> bmiChart;

    @FXML
    private CategoryAxis heightXAxis;

    @FXML
    private NumberAxis heightYAxis;

    @FXML
    private CategoryAxis weightXAxis;

    @FXML
    private NumberAxis weightYAxis;

    @FXML
    private CategoryAxis bmiXAxis;

    @FXML
    private NumberAxis bmiYAxis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var heightDataSeries = new XYChart.Series<String, Number>();
        var weightDataSeries = new XYChart.Series<String, Number>();
        var bmiDataSeries = new XYChart.Series<String, Number>();

        bodyMetricsData.forEach(data -> {
            String date = Beans.DATE_FORMAT_CONVERTER.toCustom(data.get("date").toString()).substring(0, 5);
            double height = ((Double) data.get("height")) / 100;
            double weight = (Double) data.get("weight");
            double bmi = weight / (height * height);
            heightDataSeries.getData().add(new XYChart.Data<>(date, height * 100));
            weightDataSeries.getData().add(new XYChart.Data<>(date, weight));
            bmiDataSeries.getData().add(new XYChart.Data<>(date, bmi));
            heightXAxis.setLabel("Ngày");
            heightYAxis.setLabel("Chiều cao (cm)");
            weightXAxis.setLabel("Ngày");
            weightYAxis.setLabel("Cân nặng (kg)");
            bmiXAxis.setLabel("Ngày");
            bmiYAxis.setLabel("BMI");
        });

        heightChart.setTitle("Tăng trưởng chiều cao");
        weightChart.setTitle("Tăng trưởng cân nặng");
        bmiChart.setTitle("Thay đổi BMI");
        heightChart.getData().setAll(heightDataSeries);
        weightChart.getData().setAll(weightDataSeries);
        bmiChart.getData().setAll(bmiDataSeries);
        heightChart.setLegendVisible(false);
        weightChart.setLegendVisible(false);
        bmiChart.setLegendVisible(false);
    }
}

