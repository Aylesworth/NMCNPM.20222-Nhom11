package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddInjectionController implements Initializable {

    private long childId;

    private ChildDetailsController parentController;

    public AddInjectionController(long childId, ChildDetailsController parentController) {
        this.childId = childId;
        this.parentController = parentController;
    }

    @FXML
    private JFXComboBox<String> cbxVaccine;

    @FXML
    private JFXComboBox<String> cbxDoseNo;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtStatus;

    @FXML
    private JFXTextField txtNote;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxDoseNo.setDisable(true);

        List<Map<String, Object>> data;
        ObservableList<String> vaccines = FXCollections.observableArrayList();
        try {
            data = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/vaccines")
                    .token(Utils.getToken())
                    .method("GET")
                    .build().request();
            vaccines.setAll(data.stream()
                    .map(vaccine -> vaccine.get("name").toString())
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        cbxVaccine.setItems(vaccines);

        cbxVaccine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                cbxDoseNo.setDisable(true);
                return;
            }
            cbxDoseNo.setDisable(false);
            cbxDoseNo.setItems(FXCollections.observableArrayList(
                    IntStream.rangeClosed(1, (int) data.stream()
                                    .filter(vaccine -> vaccine.get("name").equals(newValue))
                                    .count()
                            ).mapToObj(String::valueOf)
                            .toList()
            ));
        });
    }

    @FXML
    void add(ActionEvent event) {
        try {
            String requestBody = new RequestBodyMap()
                    .put("vaccineName", cbxVaccine.getValue())
                    .put("vaccineDoseNo", cbxDoseNo.getValue())
                    .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(txtDate.getText()))
                    .put("note", txtNote.getText())
                    .put("status", txtStatus.getText())
                    .toJson();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections")
                    .token(Utils.getToken())
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm mũi tiêm thành công!");
            ((Stage) txtDate.getScene().getWindow()).close();
            parentController.loadInjections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtDate.getScene().getWindow()).close();
    }

}
