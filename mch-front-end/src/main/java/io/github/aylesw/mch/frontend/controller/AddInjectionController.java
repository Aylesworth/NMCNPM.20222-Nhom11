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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
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
    private DatePicker dpDate;

    @FXML
    private JFXTextField txtStatus;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!UserIdentity.isAdmin()) {
            cbxChild.setVisible(false);
            cbxChild.setManaged(false);
            txtStatus.setText("Chờ xác nhận");
            txtStatus.setManaged(false);
        }
        cbxDoseNo.setDisable(true);
        dpDate.setConverter(Beans.DATE_STRING_CONVERTER);
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
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        try {
            long childId = ((Double) cbxChild.getSelectionModel().getSelectedItem().get("id")).longValue();
            String requestBody = new RequestBodyMap()
                    .put("vaccineName", cbxVaccine.getValue())
                    .put("vaccineDoseNo", cbxDoseNo.getValue())
                    .put("date", dpDate.getValue().toString())
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
                Utils.showAlert(Alert.AlertType.INFORMATION, "Đăng ký tiêm chủng sẽ được quản trị viên xem xét và xác nhận");
            }

            ((Stage) dpDate.getScene().getWindow()).close();
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
            if (e.getMessage().contains("already injected")) {
                lblError.setText("Trẻ đã tiêm hoặc đã đăng ký tiêm mũi tiêm này!");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        cbxChild.setStyle("");
        cbxVaccine.setStyle("");
        cbxDoseNo.setStyle("");
        dpDate.setStyle("");
        txtStatus.setStyle("");
        txtNote.setStyle("");

        if (cbxChild.getSelectionModel().getSelectedItem() == null) {
            cbxChild.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn trẻ em!");
        }
        if (cbxVaccine.getSelectionModel().getSelectedItem() == null) {
            cbxVaccine.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn vaccine!");
        }
        if (cbxDoseNo.getSelectionModel().getSelectedItem() == null) {
            cbxDoseNo.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn số mũi vaccine!");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dpDate.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày tiêm không hợp lệ!");
        }
        if (!UserIdentity.isAdmin() && date.isBefore(LocalDate.now())) {
            dpDate.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn ngày trong thời gian tới!");
        }
        if (txtStatus.getText().isBlank()) {
            txtStatus.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập trạng thái!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) dpDate.getScene().getWindow()).close();
    }

}
