package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXTextArea;
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

public class AddEventController implements Initializable {

    private ManageEventsController parentController;

    public AddEventController(ManageEventsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private JFXTextField txtMinAge;

    @FXML
    private JFXTextField txtMaxAge;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpFromDate.setConverter(Beans.DATE_STRING_CONVERTER);
        dpToDate.setConverter(Beans.DATE_STRING_CONVERTER);
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
                .put("name", txtName.getText())
                .put("description", txtDescription.getText())
                .put("minAge", Integer.parseInt(txtMinAge.getText()))
                .put("maxAge", Integer.parseInt(txtMaxAge.getText()))
                .put("fromDate", dpFromDate.getValue().toString())
                .put("toDate", dpToDate.getValue().toString())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm sự kiện thành công!");
            parentController.setUpEventList();
            ((Stage) txtName.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtName.setStyle("");
        txtDescription.setStyle("");
        dpFromDate.setStyle("");
        dpToDate.setStyle("");
        txtMinAge.setStyle("");
        txtMaxAge.setStyle("");

        if (txtName.getText().isBlank()) {
            txtName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền tên sự kiện!");
        }
        LocalDate fromDate, toDate;
        try {
            fromDate = LocalDate.parse(dpFromDate.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpFromDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày bắt đầu không hợp lệ!");
        }
        try {
            toDate = LocalDate.parse(dpToDate.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpToDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày kết thúc không hợp lệ!");
        }

        if (fromDate.isAfter(toDate)) {
            dpFromDate.setStyle(AppConstants.ERROR_BACKGROUND);
            dpToDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày kết thúc không được đứng trước ngày bắt đầu!");
        }

        if (txtMinAge.getText().isBlank()) {
            txtMinAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập độ tuổi tối thiểu!");
        }
        double minAge;
        try {
            minAge = Double.parseDouble(txtMinAge.getText());
        } catch (Exception e) {
            txtMinAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Tuổi tối thiểu không hợp lệ!");
        }
        if (minAge <= 0 || minAge > 100) {
            txtMinAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Tuổi tối thiểu không hợp lệ!");
        }

        if (txtMaxAge.getText().isBlank()) {
            txtMaxAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập độ tuổi tối thiểu!");
        }
        double maxAge;
        try {
            maxAge = Double.parseDouble(txtMaxAge.getText());
        } catch (Exception e) {
            txtMaxAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Tuổi tối đa không hợp lệ!");
        }
        if (maxAge <= 0 || maxAge > 100) {
            txtMaxAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Tuổi tối đa không hợp lệ!");
        }

        if (maxAge < minAge) {
            txtMinAge.setStyle(AppConstants.ERROR_BACKGROUND);
            txtMaxAge.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Tuổi tối đa không được nhỏ hơn tuổi tối thiểu!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtName.getScene().getWindow()).close();
    }

}
