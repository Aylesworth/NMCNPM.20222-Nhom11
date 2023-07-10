package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddExaminationController implements Initializable {

    private long childId;

    private ChildDetailsController parentController;

    public AddExaminationController(long childId, ChildDetailsController parentController) {
        this.childId = childId;
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtFacility;

    @FXML
    private JFXTextField txtReason;

    @FXML
    private JFXTextField txtResult;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private JFXListView<String> listMedicines;

    @FXML
    private JFXTextField txtMedicine;

    @FXML
    private Button btnRemoveMedicine;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Label lblError;

    private ObservableList<String> medicines;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDate.setConverter(Beans.DATE_STRING_CONVERTER);
        btnRemoveMedicine.setVisible(false);

        medicines = FXCollections.observableArrayList();
        listMedicines.setItems(medicines);
        listMedicines.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnRemoveMedicine.setVisible(true);
            } else {
                btnRemoveMedicine.setVisible(false);
            }
        });
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
                .put("date", dpDate.getValue().toString())
                .put("facility", txtFacility.getText())
                .put("reason", txtReason.getText())
                .put("result", txtResult.getText())
                .put("note", txtNote.getText())
                .put("medicines", medicines.stream().toList())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/examinations")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm thông tin khám chữa bệnh thành công!");
            parentController.loadExaminations();
            ((Stage) dpDate.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        dpDate.setStyle("");
        txtFacility.setStyle("");
        txtReason.setStyle("");
        txtResult.setStyle("");
        txtNote.setStyle("");

        LocalDate date;
        try {
            date = LocalDate.parse(dpDate.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày khám không hợp lệ!");
        }

        if (txtFacility.getText().isBlank()) {
            txtFacility.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập nơi khám!");
        }

        if (txtReason.getText().isBlank()) {
            txtReason.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập lý do khám!");
        }

        if (txtResult.getText().isBlank()) {
            txtResult.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập kết quả chẩn đoán!");
        }
    }

    @FXML
    void addMedicine(ActionEvent event) {
        lblError.setText("");
        txtMedicine.setStyle("");

        if (txtMedicine.getText().isEmpty()) {
            txtMedicine.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError.setText("Tên thuốc không hợp lệ!");
            return;
        }

        medicines.add(txtMedicine.getText());
        txtMedicine.setText("");
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) dpDate.getScene().getWindow()).close();
    }

    @FXML
    void removeMedicine(ActionEvent event) {
        medicines.remove(listMedicines.getSelectionModel().getSelectedItem());
    }

}
