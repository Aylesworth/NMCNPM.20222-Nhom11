package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddBodyMetricsController implements Initializable {

    private long childId;

    private ChildDetailsController parentController;

    public AddBodyMetricsController(long childId, ChildDetailsController parentController) {
        this.childId = childId;
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtHeight;

    @FXML
    private JFXTextField txtWeight;

    @FXML
    private DatePicker dpDate;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDate.setConverter(Beans.DATE_STRING_CONVERTER);
    }

    @FXML
    void add(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String requestBody = new RequestBodyMap()
                .put("height", Double.parseDouble(txtHeight.getText()))
                .put("weight", Double.parseDouble(txtWeight.getText()))
                .put("date", dpDate.getValue().toString())
                .put("note", txtNote.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/body-metrics")
                    .token(Utils.getToken())
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm chiều cao cân nặng thành công!");
            parentController.loadBodyMetrics();
            ((Stage) txtHeight.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtHeight.setStyle("");
        txtWeight.setStyle("");
        txtNote.setStyle("");
        dpDate.setStyle("");

        if (txtHeight.getText().isBlank()) {
            txtHeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập chiều cao!");
        }
        double height;
        try {
            height = Double.parseDouble(txtHeight.getText());
        } catch (Exception e) {
            txtHeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Chiều cao không hợp lệ!");
        }
        if (height <= 0 || height > 200) {
            txtHeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Chiều cao không hợp lệ!");
        }

        if (txtWeight.getText().isBlank()) {
            txtWeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập cân nặng!");
        }
        double weight;
        try {
            weight = Double.parseDouble(txtWeight.getText());
        } catch (Exception e) {
            txtWeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Cân nặng không hợp lệ!");
        }
        if (weight <= 0 || weight > 100) {
            txtWeight.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Cân nặng không hợp lệ!");
        }

        try {
            LocalDate.parse(dpDate.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày đo không hợp lệ!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtHeight.getScene().getWindow()).close();
    }

}

