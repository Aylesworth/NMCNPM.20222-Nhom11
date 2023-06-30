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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddExaminationController implements Initializable {

    private long childId;

    private ChildDetailsController parentController;

    public AddExaminationController(long childId, ChildDetailsController parentController) {
        this.childId = childId;
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtDate;

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

    private ObservableList<String> medicines;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        String requestBody = new RequestBodyMap()
                .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(txtDate.getText()))
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

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm thông tin khám chữa bệnh thành công!");
            parentController.loadExaminations();
            ((Stage) txtDate.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addMedicine(ActionEvent event) {
        medicines.add(txtMedicine.getText());
        txtMedicine.setText("");
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtDate.getScene().getWindow()).close();
    }

    @FXML
    void removeMedicine(ActionEvent event) {
        medicines.remove(listMedicines.getSelectionModel().getSelectedItem());
    }

}
