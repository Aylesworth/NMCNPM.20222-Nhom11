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
import javafx.util.StringConverter;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddInjectionController implements Initializable {

    private long childId;

    private Object parentController;

    public AddInjectionController(long childId, Object parentController) {
        this.childId = childId;
        this.parentController = parentController;
    }

    public AddInjectionController(Object parentController) {
        this(-1, parentController);
    }

    @FXML
    private JFXComboBox<Map<String, Object>> cbxChild;

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
        if (!UserIdentity.isAdmin()) {
            cbxChild.setDisable(true);
            txtStatus.setText("Chờ xác nhận");
            txtStatus.setEditable(false);
        }
        cbxDoseNo.setDisable(true);

        setUpVaccineComboBoxes();
        setUpChildComboBox();
    }

    private void setUpVaccineComboBoxes() {
        List<Map<String, Object>> data;
        ObservableList<String> vaccines = FXCollections.observableArrayList();
        try {
            data = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/vaccines")
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

    private void setUpChildComboBox() {
        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children")
                    .method("GET")
                    .build().request();

            cbxChild.setItems(FXCollections.observableArrayList(result));
            cbxChild.setCellFactory(view -> new JFXListCell<>() {
                @Override
                protected void updateItem(Map<String, Object> item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item != null) {
                        setText(item.get("fullName").toString());
                    }
                }
            });
            cbxChild.setConverter(new StringConverter<Map<String, Object>>() {
                @Override
                public String toString(Map<String, Object> stringObjectMap) {
                    return stringObjectMap.get("fullName").toString();
                }

                @Override
                public Map<String, Object> fromString(String s) {
                    return null;
                }
            });

            if (childId != -1) {
                cbxChild.getSelectionModel().select(cbxChild.getItems()
                        .filtered(map -> ((Double) map.get("id")).longValue() == childId)
                        .get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void add(ActionEvent event) {
        try {
            long childId = ((Double) cbxChild.getSelectionModel().getSelectedItem().get("id")).longValue();
            String requestBody = new RequestBodyMap()
                    .put("vaccineName", cbxVaccine.getValue())
                    .put("vaccineDoseNo", cbxDoseNo.getValue())
                    .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(txtDate.getText()))
                    .put("note", txtNote.getText())
                    .put("status", txtStatus.getText())
                    .toJson();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Thêm mũi tiêm thành công!");

            if (!UserIdentity.isAdmin()) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Đăng ký tiêm chủng sẽ được quản trị viên xem xét và phê duyệt");
            }

            ((Stage) txtDate.getScene().getWindow()).close();
            switch (parentController) {
                case ChildDetailsController childDetailsController -> {
                    childDetailsController.loadInjections();
                }
                case ManageInjectionsController manageInjectionsController -> {
                    manageInjectionsController.loadScheduleData();
                    manageInjectionsController.loadRegistrations();
                }
                default -> throw new IllegalStateException("Unexpected value: " + parentController);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtDate.getScene().getWindow()).close();
    }

}
