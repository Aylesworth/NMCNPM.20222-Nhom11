package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Beans;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class BodyMetricsItemController implements Initializable {

    Map<String, Object> properties;

    ChildDetailsController parentController;

    public BodyMetricsItemController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
        this.parentController = parentController;
    }

    @FXML
    private Label lblHeight;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNote;

    @FXML
    private Label lblWeight;

    @FXML
    private Label lblBmi;

    @FXML
    private Label lblEvaluation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double height = (Double) properties.get("height");
        double weight = (Double) properties.get("weight");
        lblHeight.setText("%.0f cm".formatted(height));
        lblWeight.setText("%.1f kg".formatted(weight));
        lblDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("date").toString()));
        lblNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());
        lblNote.setTooltip(lblNote.getText().equals("") ? new Tooltip("không có") : new Tooltip(lblNote.getText()));
        double bmi = calculateBMI(height, weight);
        lblBmi.setText("%.2f".formatted(bmi));
        lblEvaluation.setText(getBMICategory(bmi));
    }

    @FXML
    void delete(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa thông tin này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            long id = ((Double) properties.get("id")).longValue();
            long childId = ((Double) properties.get("childId")).longValue();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/body-metrics/" + id)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa chiều cao cân nặng thành công!");
            parentController.loadBodyMetrics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateBMI(double height, double weight) {
        height /= 100;
        return weight / (height * height);
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Gầy";
        if (bmi < 25.0) return "Bình thường";
        if (bmi < 30.0) return "Thừa cân";
        return "Béo phì";
    }

}
