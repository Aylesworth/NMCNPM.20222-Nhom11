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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditExaminationController implements Initializable {

    private Map<String, Object> properties;

    private ChildDetailsController parentController;

    public EditExaminationController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
        this.parentController = parentController;
    }

    @FXML
    private DatePicker dpDate;

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
    private Label lblError;

    private ObservableList<String> medicines;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDate.setConverter(Beans.DATE_STRING_CONVERTER);
        btnRemoveMedicine.setVisible(false);

        dpDate.setValue(LocalDate.parse(properties.get("date").toString()));
        txtFacility.setText(properties.get("facility").toString());
        txtReason.setText(properties.get("reason").toString());
        txtResult.setText(properties.get("result").toString());
        txtNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());
        medicines = FXCollections.observableArrayList((List<String>) properties.get("medicines"));

        listMedicines.setItems(medicines);
        listMedicines.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnRemoveMedicine.setVisible(newValue != null);
        });
    }

    @FXML
    void save(ActionEvent event) {

        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        long childId = ((Double) properties.get("childId")).longValue();
        long id = ((Double) properties.get("id")).longValue();
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
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/examinations/" + id)
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật thông tin khám chữa bệnh thành công!");
            parentController.loadExaminations();
            ((Stage) dpDate.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addMedicine(ActionEvent event) {
        lblError.setText("");
        txtMedicine.setStyle("");

        if (txtMedicine.getText().isBlank()) {
            txtMedicine.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError.setText("Tên thuốc không hợp lệ!");
            return;
        }

        medicines.add(txtMedicine.getText());
        txtMedicine.setText("");
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
    void cancel(ActionEvent event) {
        ((Stage) dpDate.getScene().getWindow()).close();
    }

    @FXML
    void removeMedicine(ActionEvent event) {
        medicines.remove(listMedicines.getSelectionModel().getSelectedItem());
    }

}
