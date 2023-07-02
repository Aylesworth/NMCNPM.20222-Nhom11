package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AddBodyMetricsController {

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
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtNote;

    @FXML
    void add(ActionEvent event) {
        String requestBody = new RequestBodyMap()
                .put("height", Double.parseDouble(txtHeight.getText()))
                .put("weight", Double.parseDouble(txtWeight.getText()))
                .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(txtDate.getText()))
                .put("note", txtNote.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/body-metrics")
                    .token(Utils.getToken())
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm chiều cao cân nặng thành công!");
            parentController.loadBodyMetrics();
            ((Stage) txtHeight.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtHeight.getScene().getWindow()).close();
    }

}

