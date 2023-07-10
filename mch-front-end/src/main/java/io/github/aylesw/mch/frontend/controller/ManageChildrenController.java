package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageChildrenController implements Initializable {

    @FXML
    private TabPane root;

    @FXML
    private TableView<Map<String, Object>> tblChildren;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private Label lblId;

    @FXML
    private JFXTextField txtEthnicity;

    @FXML
    private DatePicker dpDob;

    @FXML
    private JFXTextField txtBirthplace;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnViewProfile;

    @FXML
    private TableView<Map<String, Object>> tblChildRegistrations;

    @FXML
    private ProgressIndicator spinner2;

    @FXML
    private JFXTextField txtFullName2;

    @FXML
    private JFXTextField txtEthnicity2;

    @FXML
    private JFXTextField txtDob2;

    @FXML
    private JFXTextField txtBirthplace2;

    @FXML
    private JFXTextField txtInsuranceId2;

    @FXML
    private JFXButton btnRejectRegistration;

    @FXML
    private JFXButton btnApproveRegistration;

    @FXML
    private JFXTextField txtSex2;

    @FXML
    private TableView<Map<String, Object>> tblChildChanges;

    @FXML
    private ProgressIndicator spinner3;

    @FXML
    private JFXTextField txtNewFullName;

    @FXML
    private JFXTextField txtNewEthnicity;

    @FXML
    private JFXTextField txtNewDob;

    @FXML
    private JFXTextField txtNewBirthplace;

    @FXML
    private JFXTextField txtNewInsuranceId;

    @FXML
    private JFXButton btnRejectChange;

    @FXML
    private JFXButton btnApproveChange;

    @FXML
    private JFXTextField txtOrgFullName;

    @FXML
    private JFXTextField txtOrgEthnicity;

    @FXML
    private JFXTextField txtOrgDob;

    @FXML
    private JFXTextField txtOrgBirthplace;

    @FXML
    private JFXTextField txtOrgInsuranceId;

    @FXML
    private JFXTextField txtOrgSex;

    @FXML
    private JFXTextField txtNewSex;

    @FXML
    private JFXTextField txtSearchChild;

    @FXML
    private JFXTextField txtSearchRegistration;

    @FXML
    private JFXTextField txtSearchChange;

    @FXML
    private JFXButton btnViewProfile2;

    @FXML
    private JFXTextField txtParent;

    @FXML
    private Label lblError;

    private ObservableList<Map<String, Object>> children;
    private ObservableList<Map<String, Object>> registrations;
    private ObservableList<Map<String, Object>> changes;
    private long selectedChildId;
    private long selectedRegistrationId;
    private long selectedChangeId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);

        loadChildrenData();
        loadRegistrationsData();
        loadChangesData();
    }

    private void clearChildInfo() {
        lblId.setText("ID: ");
        txtFullName.setText("");
        cbxSex.getSelectionModel().clearSelection();
        dpDob.setValue(null);
        txtEthnicity.setText("");
        txtBirthplace.setText("");
        txtInsuranceId.setText("");

        btnUpdate.setVisible(false);
        btnViewProfile.setVisible(false);
        btnDelete.setVisible(false);
    }

    void loadChildrenData() {
        clearChildInfo();

        children = FXCollections.observableArrayList();
        tblChildren.setItems(children);

        tblChildren.getColumns().clear();

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        TableColumn<Map<String, Object>, String> ageCol = new TableColumn<>("Tuổi (tháng)");
        ageCol.setCellValueFactory(e -> new SimpleStringProperty(((Double) e.getValue().get("ageInMonths")).longValue() + ""));

        tblChildren.getColumns().addAll(fullNameCol, ageCol);

        tblChildren.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearChildInfo();
            } else {
                selectedChildId = ((Double) newValue.get("id")).longValue();

                lblId.setText("ID: " + ((Double) newValue.get("id")).longValue());
                txtFullName.setText(newValue.get("fullName").toString());
                cbxSex.getSelectionModel().selectFirst();
                while (!cbxSex.getSelectionModel().getSelectedItem().equals(newValue.get("sex").toString()))
                    cbxSex.getSelectionModel().selectNext();
                dpDob.setValue(LocalDate.parse(newValue.get("dob").toString()));
                txtEthnicity.setText(Optional.ofNullable(newValue.get("ethnicity")).orElse("").toString());
                txtBirthplace.setText(Optional.ofNullable(newValue.get("birthplace")).orElse("").toString());
                txtInsuranceId.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());

                btnUpdate.setVisible(true);
                btnViewProfile.setVisible(true);
                btnDelete.setVisible(true);
            }
        });

        spinner1.setVisible(true);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(AppConstants.BASE_URL + "/children")
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();
                            return FXCollections.observableArrayList(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                children.setAll(service.getValue());
                spinner1.setVisible(false);
            }
        });

        service.start();
    }

    private void clearRegistrationInfo() {
        txtFullName2.setText("");
        txtSex2.setText("");
        txtDob2.setText("");
        txtEthnicity2.setText("");
        txtBirthplace2.setText("");
        txtInsuranceId2.setText("");

        btnApproveRegistration.setVisible(false);
        btnRejectRegistration.setVisible(false);
    }

    void loadRegistrationsData() {
        clearRegistrationInfo();

        registrations = FXCollections.observableArrayList();
        tblChildRegistrations.setItems(registrations);

        tblChildRegistrations.getColumns().clear();

        TableColumn<Map<String, Object>, String> timeCol = new TableColumn<>("Thời gian");
        timeCol.setCellValueFactory(e -> new SimpleStringProperty(Beans.TIME_FORMAT_CONVERTER.toCustom(e.getValue().get("time").toString())));

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        TableColumn<Map<String, Object>, String> ageCol = new TableColumn<>("Tuổi (tháng)");
        ageCol.setCellValueFactory(e -> new SimpleStringProperty(((Double) e.getValue().get("ageInMonths")).longValue() + ""));

        tblChildRegistrations.getColumns().addAll(fullNameCol, ageCol);

        tblChildRegistrations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearRegistrationInfo();
            } else {
                selectedRegistrationId = ((Double) newValue.get("id")).longValue();

                txtFullName2.setText(newValue.get("fullName").toString());
                txtSex2.setText(newValue.get("sex").toString());
                txtDob2.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("dob").toString()));
                txtEthnicity2.setText(Optional.ofNullable(newValue.get("ethnicity")).orElse("").toString());
                txtBirthplace2.setText(Optional.ofNullable(newValue.get("birthplace")).orElse("").toString());
                txtInsuranceId2.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());
                txtParent.setText(((Map<String, Object>) newValue.get("parent")).get("fullName").toString());

                btnApproveRegistration.setVisible(true);
                btnRejectRegistration.setVisible(true);
            }
        });

        spinner2.setVisible(true);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(AppConstants.BASE_URL + "/children/pending-registrations")
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();
                            return FXCollections.observableArrayList(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                registrations.setAll(service.getValue());
                spinner2.setVisible(false);
            }
        });

        service.start();
    }

    private void clearChangeInfo() {
        txtOrgFullName.setText("");
        txtOrgFullName.setStyle("");
        txtOrgSex.setText("");
        txtOrgSex.setStyle("");
        txtOrgDob.setText("");
        txtOrgDob.setStyle("");
        txtOrgEthnicity.setText("");
        txtOrgEthnicity.setStyle("");
        txtOrgBirthplace.setText("");
        txtOrgBirthplace.setStyle("");
        txtOrgInsuranceId.setText("");
        txtOrgInsuranceId.setStyle("");

        txtNewFullName.setText("");
        txtNewFullName.setStyle("");
        txtNewSex.setText("");
        txtNewSex.setStyle("");
        txtNewDob.setText("");
        txtNewDob.setStyle("");
        txtNewEthnicity.setText("");
        txtNewEthnicity.setStyle("");
        txtNewBirthplace.setText("");
        txtNewBirthplace.setStyle("");
        txtNewInsuranceId.setText("");
        txtNewInsuranceId.setStyle("");

        btnApproveChange.setVisible(false);
        btnRejectChange.setVisible(false);
        btnViewProfile2.setVisible(false);
    }

    void loadChangesData() {
        clearChangeInfo();

        changes = FXCollections.observableArrayList();
        tblChildChanges.setItems(changes);

        tblChildChanges.getColumns().clear();

        TableColumn<Map<String, Object>, String> timeCol = new TableColumn<>("Thời gian");
        timeCol.setCellValueFactory(e -> new SimpleStringProperty(Beans.TIME_FORMAT_CONVERTER.toCustom(e.getValue().get("time").toString())));

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        TableColumn<Map<String, Object>, String> ageCol = new TableColumn<>("Tuổi (tháng)");
        ageCol.setCellValueFactory(e -> new SimpleStringProperty(((Double) ((Map<String, Object>) e.getValue().get("child")).get("ageInMonths")).longValue() + ""));

        tblChildChanges.getColumns().addAll(fullNameCol, ageCol);

        tblChildChanges.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearChangeInfo();
            } else {
                clearChangeInfo();
                selectedChangeId = ((Double) newValue.get("id")).longValue();

                txtNewFullName.setText(newValue.get("fullName").toString());
                txtNewSex.setText(newValue.get("sex").toString());
                txtNewDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("dob").toString()));
                txtNewEthnicity.setText(Optional.ofNullable(newValue.get("ethnicity")).orElse("").toString());
                txtNewBirthplace.setText(Optional.ofNullable(newValue.get("birthplace")).orElse("").toString());
                txtNewInsuranceId.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());

                var original = (Map<String, Object>) newValue.get("child");

                txtOrgFullName.setText(original.get("fullName").toString());
                txtOrgSex.setText(original.get("sex").toString());
                txtOrgDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(original.get("dob").toString()));
                txtOrgEthnicity.setText(Optional.ofNullable(original.get("ethnicity")).orElse("").toString());
                txtOrgBirthplace.setText(Optional.ofNullable(original.get("birthplace")).orElse("").toString());
                txtOrgInsuranceId.setText(Optional.ofNullable(original.get("insuranceId")).orElse("").toString());

                btnApproveChange.setVisible(true);
                btnRejectChange.setVisible(true);
                btnViewProfile2.setVisible(true);

                String changeBackground = "-fx-background-color: #ffffe6";
                if (!txtOrgFullName.getText().equals(txtNewFullName.getText())) {
                    txtOrgFullName.setStyle(changeBackground);
                    txtNewFullName.setStyle(changeBackground);
                }
                if (!txtOrgSex.getText().equals(txtNewSex.getText())) {
                    txtOrgSex.setStyle(changeBackground);
                    txtNewSex.setStyle(changeBackground);
                }
                if (!txtOrgDob.getText().equals(txtNewDob.getText())) {
                    txtOrgDob.setStyle(changeBackground);
                    txtNewDob.setStyle(changeBackground);
                }
                if (!txtOrgEthnicity.getText().equals(txtNewEthnicity.getText())) {
                    txtOrgEthnicity.setStyle(changeBackground);
                    txtNewEthnicity.setStyle(changeBackground);
                }
                if (!txtOrgBirthplace.getText().equals(txtNewBirthplace.getText())) {
                    txtOrgBirthplace.setStyle(changeBackground);
                    txtNewBirthplace.setStyle(changeBackground);
                }
                if (!txtOrgInsuranceId.getText().equals(txtNewInsuranceId.getText())) {
                    txtOrgInsuranceId.setStyle(changeBackground);
                    txtNewInsuranceId.setStyle(changeBackground);
                }
            }
        });

        spinner3.setVisible(true);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(AppConstants.BASE_URL + "/children/pending-changes")
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();
                            return FXCollections.observableArrayList(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                changes.setAll(service.getValue());
                spinner3.setVisible(false);
            }
        });

        service.start();
    }

    @FXML
    void approveChange(ActionEvent event) {
        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/approve-change?id=" + selectedChangeId)
                    .token(Utils.getToken())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Phê duyệt thay đổi hồ sơ thành công!");
            loadChangesData();
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void approveRegistration(ActionEvent event) {
        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/approve-registration?id=" + selectedRegistrationId)
                    .token(Utils.getToken())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Phê duyệt hồ sơ trẻ thành công!");
            loadRegistrationsData();
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteChild(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa hồ sơ trẻ em này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + selectedChildId)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa hồ sơ trẻ em thành công!");
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void rejectChange(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lý do");
        dialog.setHeaderText("Vui lòng nhập lý do từ chối:");
        dialog.setContentText("");
        var reason = dialog.showAndWait();
        while (reason.isEmpty() || reason.get().isBlank()) {
            if (reason.isEmpty()) return;
            Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
            reason = dialog.showAndWait();
        }

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/reject-change?id=" + selectedChangeId + "&reason=" + reason.get())
                    .token(Utils.getToken())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Đã từ chối thay đổi thông tin hồ sơ.");
            loadChangesData();
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void rejectRegistration(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lý do");
        dialog.setHeaderText("Vui lòng nhập lý do từ chối:");
        dialog.setContentText("");
        var reason = dialog.showAndWait();
        while (reason.isEmpty() || reason.get().isBlank()) {
            if (reason.isEmpty()) return;
            Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
            reason = dialog.showAndWait();
        }

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/reject-registration?id=" + selectedRegistrationId + "&reason=" + reason.get())
                    .token(Utils.getToken())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Đã từ chối phê duyệt hồ sơ trẻ.");
            loadRegistrationsData();
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void searchChild(KeyEvent event) {
        tblChildren.setItems(children.filtered(e ->
                e.get("fullName").toString().toLowerCase()
                        .contains(txtSearchChild.getText().toLowerCase())));
    }

    @FXML
    void searchChildRegistration(KeyEvent event) {
        tblChildRegistrations.setItems(registrations.filtered(e ->
                e.get("fullName").toString().toLowerCase()
                        .contains(txtSearchRegistration.getText().toLowerCase())));
    }

    @FXML
    void searchChildChange(KeyEvent event) {
        tblChildChanges.setItems(changes.filtered(e ->
                e.get("fullName").toString().toLowerCase()
                        .contains(txtSearchRegistration.getText().toLowerCase())));
    }

    @FXML
    void addChild(ActionEvent event) {
        ScreenManager.getAddChildStage(this).show();
    }

    @FXML
    void updateChild(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        var requestBody = new RequestBodyMap()
                .put("fullName", txtFullName.getText().toString())
                .put("sex", cbxSex.getSelectionModel().getSelectedItem())
                .put("dob", dpDob.getValue().toString())
                .put("ethnicity", txtEthnicity.getText())
                .put("birthplace", txtBirthplace.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + selectedChildId)
                    .token(Utils.getToken())
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật hồ sơ trẻ em thành công!");
            loadChildrenData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtFullName.setStyle("");
        cbxSex.setStyle("");
        dpDob.setStyle("");
        txtEthnicity.setStyle("");
        txtBirthplace.setStyle("");
        txtInsuranceId.setStyle("");

        if (txtFullName.getText().isBlank()) {
            txtFullName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền họ tên!");
        }
        if (cbxSex.getSelectionModel().getSelectedItem() == null) {
            cbxSex.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn giới tính!");
        }
        try {
            LocalDate.parse(dpDob.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDob.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày sinh không hợp lệ!");
        }
        if (!txtInsuranceId.getText().isBlank() && !txtInsuranceId.getText().matches("[A-Z]{2}[0-9]{13}")) {
            txtInsuranceId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số BHYT khỗng hợp lệ!");
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(selectedChildId, root, this));
    }

    @FXML
    void viewProfile2(ActionEvent event) {
        long id = ((Double) ((Map<String, Object>) (tblChildChanges.getSelectionModel().getSelectedItem()).get("child")).get("id")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(id, root, this));
    }

}
