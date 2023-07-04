package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageInjectionsController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tblSchedule;

    @FXML
    private JFXButton btnChangeDate;

    @FXML
    private JFXButton btnDeleteInjection;

    @FXML
    private JFXButton btnViewProfile;

    @FXML
    private TableView<Map<String, Object>> tblRegistrations;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtVaccine;

    @FXML
    private JFXTextField txtDoseNo;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private JFXButton btnConfirm;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnViewProfile2;

    private ObservableList<Map<String, Object>> schedule;
    private ObservableList<Map<String, Object>> registrations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadScheduleData();
        loadRegistrations();
    }

    private void hideButtons() {
        btnChangeDate.setVisible(false);
        btnDeleteInjection.setVisible(false);
        btnViewProfile.setVisible(false);
    }

    private void showButtons() {
        btnChangeDate.setVisible(true);
        btnDeleteInjection.setVisible(true);
        btnViewProfile.setVisible(true);
    }

    void loadScheduleData() {
        hideButtons();
        schedule = FXCollections.observableArrayList();
        tblSchedule.setItems(schedule);

        var childNameCol = new TableColumn<Map<String, Object>, String>("Tên trẻ");
        childNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("childName").toString()));

        var vaccineCol = new TableColumn<Map<String, Object>, String>("Vaccine");
        vaccineCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("vaccineName").toString()));

        var doseNoCol = new TableColumn<Map<String, Object>, String>("Mũi số");
        doseNoCol.setCellValueFactory(data -> new SimpleStringProperty(((Double) data.getValue().get("vaccineDoseNo")).longValue() + ""));

        var dateCol = new TableColumn<Map<String, Object>, String>("Ngày tiêm");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(Beans.DATE_FORMAT_CONVERTER.toCustom(data.getValue().get("date").toString())));

        tblSchedule.getColumns().setAll(childNameCol, vaccineCol, doseNoCol, dateCol);

        tblSchedule.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                hideButtons();
            } else {
                showButtons();
            }
        });

        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/schedule")
                    .method("GET")
                    .build().request();

            schedule.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void hideInfoFields() {
        txtName.setVisible(false);
        txtDob.setVisible(false);
        txtAge.setVisible(false);
        txtVaccine.setVisible(false);
        txtDoseNo.setVisible(false);
        txtDate.setVisible(false);
        txtNote.setVisible(false);
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
        btnViewProfile2.setVisible(false);
    }

    private void showInfoFields() {
        txtName.setVisible(true);
        txtDob.setVisible(true);
        txtAge.setVisible(true);
        txtVaccine.setVisible(true);
        txtDoseNo.setVisible(true);
        txtDate.setVisible(true);
        txtNote.setVisible(true);
        btnConfirm.setVisible(true);
        btnCancel.setVisible(true);
        btnViewProfile2.setVisible(true);
    }

    void loadRegistrations() {
        hideInfoFields();
        registrations = FXCollections.observableArrayList();
        tblRegistrations.setItems(registrations);

        var childNameCol = new TableColumn<Map<String, Object>, String>("Tên trẻ");
        childNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("childName").toString()));

        var vaccineCol = new TableColumn<Map<String, Object>, String>("Vaccine");
        vaccineCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("vaccineName").toString()));

        var doseNoCol = new TableColumn<Map<String, Object>, String>("Mũi số");
        doseNoCol.setCellValueFactory(data -> new SimpleStringProperty(((Double) data.getValue().get("vaccineDoseNo")).longValue() + ""));

        var dateCol = new TableColumn<Map<String, Object>, String>("Ngày tiêm");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(Beans.DATE_FORMAT_CONVERTER.toCustom(data.getValue().get("date").toString())));

        tblRegistrations.getColumns().setAll(childNameCol, vaccineCol, doseNoCol, dateCol);

        tblRegistrations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                hideInfoFields();
            } else {
                txtName.setText(newValue.get("childName").toString());
                txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("childDob").toString()));
                txtAge.setText(((Double) newValue.get("childAgeInMonths")).longValue() + "");
                txtVaccine.setText(newValue.get("vaccineName").toString());
                txtDoseNo.setText(((Double) newValue.get("vaccineDoseNo")).longValue() + "");
                txtDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("date").toString()));
                txtNote.setText(Optional.ofNullable(newValue.get("note")).orElse("").toString());
                showInfoFields();
            }
        });

        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/pending-registrations")
                    .method("GET")
                    .build().request();

            registrations.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @FXML
    void addInjection(ActionEvent event) {
        ScreenManager.getAddInjectionStage(this).show();
    }

    @FXML
    void cancel(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Lý do");
            dialog.setHeaderText("Nhập lý do hủy đăng ký mũi tiêm:");
            dialog.setContentText(null);
            var input = dialog.showAndWait();

            while (input.isEmpty() || input.get().isBlank()) {
                if (input.isEmpty()) return;
                Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
                input = dialog.showAndWait();
            }

            String reason = input.get();
            long id = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/injections/reject-registration?id=" + id + "&reason=" + reason)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Hủy đăng ký tiêm chủng thành công!");
            loadRegistrations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeDate(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thay đổi ngày");
        dialog.setHeaderText("Vui lòng nhập ngày mới (DD/MM/YYYY):");
        dialog.setContentText("");
        var input = dialog.showAndWait();

        while (true) {
            if (input.isEmpty()) return;

            try {
                LocalDate.parse(input.get(), Beans.DATE_FORMATTER);
                break;
            } catch (Exception e) {
                Utils.showAlert(Alert.AlertType.ERROR, "Ngày không hợp lệ!");
                input = dialog.showAndWait();
            }
        }

        var selectedItem = tblSchedule.getSelectionModel().getSelectedItem();

        String requestBody = new RequestBodyMap(selectedItem)
                .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(input.get()))
                .toJson();

        long childId = ((Double) selectedItem.get("childId")).longValue();
        long id = ((Double) selectedItem.get("id")).longValue();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật ngày tiêm thành công!");
            loadScheduleData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        try {
            long id = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/injections/approve-registration?id=" + id)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xác nhận đăng ký tiêm chủng thành công!");
            loadScheduleData();
            loadRegistrations();
        } catch (Exception e) {
            if (e.getMessage().contains("expired")) {
                Utils.showAlert(Alert.AlertType.ERROR, "Không thể xác nhận do đã qua ngày đăng ký tiêm!");
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void deleteInjection(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa mũi tiêm này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        long id = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("id")).longValue();
        long childId = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("childId")).longValue();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa mũi tiêm thành công!");
            loadScheduleData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        long childId = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("childId")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(
                childId,
                tblSchedule.getParent().getParent().getParent().getParent().getParent(),
                this));
    }

    @FXML
    void viewProfile2(ActionEvent event) {
        long childId = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("childId")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(
                childId,
                tblSchedule.getParent().getParent().getParent().getParent().getParent(),
                this));
    }

    @FXML
    void exportSchedule(ActionEvent actionEvent) {
//TODO:
    }

}
