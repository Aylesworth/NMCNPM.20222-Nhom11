package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Beans;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable {

    @FXML
    private Label lblNumUsers;

    @FXML
    private Label lblUserIncrease;

    @FXML
    private Label lblNumChildren;

    @FXML
    private Label lblChildIncrease;

    @FXML
    private Label lblNumInjections;

    @FXML
    private Label lblInjectionIncrease;

    @FXML
    private BarChart<String, Long> barChart;

    @FXML
    private LineChart<String, Long> lineChart;

    private Map<String, Object> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            data = new ApiRequest.Builder<Map<String, Object>>()
                    .url(AppConstants.BASE_URL + "/statistics/overall")
                    .method("GET")
                    .build().request();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        loadFigures();
        loadBarChartData();
        loadLineChartData();
    }

    private void loadFigures() {
        lblNumUsers.setText("%,d".formatted(Utils.toLongValue(data.get("numberOfUsers"))));
        lblUserIncrease.setText("%+d".formatted(Utils.toLongValue(data.get("userIncreaseByLastMonth"))));

        lblNumChildren.setText("%,d".formatted(Utils.toLongValue(data.get("numberOfChildren"))));
        lblChildIncrease.setText("%+d".formatted(Utils.toLongValue(data.get("childIncreaseByLastMonth"))));

        lblNumInjections.setText("%,d".formatted(Utils.toLongValue(data.get("numberOfInjections"))));
        lblInjectionIncrease.setText("%+d".formatted(Utils.toLongValue(data.get("injectionIncreaseByLastMonth"))));
    }

    @FXML
    private CategoryAxis barXAxis;

    @FXML
    private NumberAxis barYAxis;

    private void loadBarChartData() {
        var vaccineData = (List<Map<String, Object>>) data.get("vaccineStatistics");

        XYChart.Series<String, Long> dataSeries = new XYChart.Series<>();

        vaccineData.forEach(data -> {
            String label = data.get("vaccine").toString();
            dataSeries.getData().add(new XYChart.Data<>(
                    label.length() > 30 ? label.substring(0, 30) + "..." : label,
                    Utils.toLongValue(data.get("quantity"))
            ));
        });

        barChart.setTitle("Số liều vaccine được sử dụng");
        barChart.getData().add(dataSeries);
        barChart.setLegendVisible(false);
        barXAxis.setTickLabelRotation(55);
    }

    @FXML
    private CategoryAxis lineXAxis;

    @FXML
    private NumberAxis lineYAxis;

    private void loadLineChartData() {
        var injectionData = (List<Map<String, Object>>) data.get("injectionStatistics");

        XYChart.Series<String, Long> dataSeries = new XYChart.Series<>();

        injectionData.forEach(data -> {
            dataSeries.getData().add(new XYChart.Data<>(
                    Beans.DATE_FORMAT_CONVERTER.toCustom(data.get("date").toString()).substring(0, 5),
                    Utils.toLongValue(data.get("count"))
            ));
        });

        lineChart.setTitle("Số mũi tiêm 7 ngày gần đây");
        lineChart.getData().add(dataSeries);
        lineChart.setLegendVisible(false);
    }
}
