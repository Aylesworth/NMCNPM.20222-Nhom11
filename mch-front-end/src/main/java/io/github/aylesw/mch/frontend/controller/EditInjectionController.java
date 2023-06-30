package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXComboBox;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditInjectionController implements Initializable {
    private Map<String, Object> properties;

    private ChildDetailsController parentController;

    public EditInjectionController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
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

        cbxVaccine.getSelectionModel()
                .select(vaccines.filtered(vaccine -> vaccine.equals(properties.get("vaccineName").toString())).get(0));
        cbxDoseNo.getSelectionModel()
                .select(cbxDoseNo.getItems().filtered(doseNo -> doseNo.equals(((Double) properties.get("vaccineDoseNo")).longValue() + "")).get(0));
        txtDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("date").toString()));
        txtNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());
        txtStatus.setText(properties.get("status").toString());
    }

    @FXML
    void save(ActionEvent event) {
        try {
            long childId = ((Double) properties.get("childId")).longValue();
            long id = ((Double) properties.get("id")).longValue();
            String requestBody = new RequestBodyMap()
                    .put("vaccineName", cbxVaccine.getValue())
                    .put("vaccineDoseNo", cbxDoseNo.getValue())
                    .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(txtDate.getText()))
                    .put("note", txtNote.getText())
                    .put("status", txtStatus.getText())
                    .toJson();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .token(Utils.getToken())
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật thông tin mũi tiêm thành công!");
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

