package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import io.github.aylesw.mch.frontend.controller.ScreenManager;
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
import java.util.*;

public class ManageUsersController implements Initializable {

    private ObservableList<Map<String, Object>> users;
    private ObservableList<Map<String, Object>> userRegistrations;
    private ObservableList<Map<String, Object>> userChanges;

    @FXML
    private TabPane root;

    @FXML
    private TableView<Map<String, Object>> tblUsers;

    @FXML
    private JFXTextField txtSearchUser;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private Label lblId;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private DatePicker dpDob;

    @FXML
    private JFXTextField txtCitizenId;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private JFXButton btnDeleteUser;

    @FXML
    private JFXButton btnUpdateUser;

    @FXML
    private JFXButton btnViewProfile;

    @FXML
    private TableView<Map<String, Object>> tblUserRegistrations;

    @FXML
    private JFXTextField txtSearchUserRegistration;

    @FXML
    private JFXTextField txtSearchUserChange;

    @FXML
    private JFXTextField txtFullName2;

    @FXML
    private JFXTextField txtEmail2;

    @FXML
    private JFXTextField txtPhoneNumber2;

    @FXML
    private JFXTextField txtAddress2;

    @FXML
    private JFXTextField txtDob2;

    @FXML
    private JFXTextField txtCitizenId2;

    @FXML
    private JFXTextField txtInsuranceId2;

    @FXML
    private JFXTextField txtSex2;

    @FXML
    private JFXButton btnRejectRegistration;

    @FXML
    private JFXButton btnApproveRegistration;

    @FXML
    private TableView<Map<String, Object>> tblUserChanges;

    @FXML
    private JFXTextField txtSearchChanges;

    @FXML
    private JFXTextField txtOrgFullName;

    @FXML
    private JFXTextField txtOrgEmail;

    @FXML
    private JFXTextField txtOrgPhoneNumber;

    @FXML
    private JFXTextField txtOrgAddress;

    @FXML
    private JFXTextField txtOrgDob;

    @FXML
    private JFXTextField txtOrgCitizenId;

    @FXML
    private JFXTextField txtOrgInsuranceId;

    @FXML
    private JFXTextField txtOrgSex;

    @FXML
    private JFXTextField txtNewFullName;

    @FXML
    private JFXTextField txtNewEmail;

    @FXML
    private JFXTextField txtNewPhoneNumber;

    @FXML
    private JFXTextField txtNewAddress;

    @FXML
    private JFXTextField txtNewDob;

    @FXML
    private JFXTextField txtNewCitizenId;

    @FXML
    private JFXTextField txtNewInsuranceId;

    @FXML
    private JFXTextField txtNewSex;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private ProgressIndicator spinner2;

    @FXML
    private ProgressIndicator spinner3;

    @FXML
    private JFXButton btnApproveChange;

    @FXML
    private JFXButton btnRejectChange;

    @FXML
    private Label lblError;

    private long selectedUserId;
    private long selectedUserRegistrationId;
    private long selectedUserChangeId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);
        loadUsersData();
        loadUserRegistrationsData();
        loadUserChangesData();
    }

    private void clearUserInfo() {
        lblId.setText("ID: ");
        txtFullName.setText("");
        txtEmail.setText("");
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        cbxSex.getSelectionModel().clearSelection();
        dpDob.setValue(null);
        txtPhoneNumber.setText("");
        txtAddress.setText("");
        txtCitizenId.setText("");
        txtInsuranceId.setText("");

        btnUpdateUser.setVisible(false);
        btnViewProfile.setVisible(false);
        btnDeleteUser.setVisible(false);
    }

    void loadUsersData() {
        String url = AppConstants.BASE_URL + "/users";
        String token = Utils.getToken();
        String method = "GET";

        users = FXCollections.observableArrayList();

        spinner1.setVisible(true);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(url).token(token).method(method)
                                    .build().request();
                            result.removeIf(e -> e.get("email").toString().equals("admin"));
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
                users.setAll(service.getValue());
                spinner1.setVisible(false);
            }
        });
        service.start();

        tblUsers.setItems(users);

        tblUsers.getColumns().clear();

        TableColumn<Map<String, Object>, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("email").toString()));

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        tblUsers.getColumns().addAll(fullNameCol, emailCol);

        clearUserInfo();

        tblUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearUserInfo();
            } else {
                btnUpdateUser.setVisible(true);
                btnViewProfile.setVisible(true);
                btnDeleteUser.setVisible(true);

                lblError.setText("");
                txtEmail.setStyle("");
                txtFullName.setStyle("");
                cbxSex.setStyle("");
                dpDob.setStyle("");
                txtPhoneNumber.setStyle("");
                txtAddress.setStyle("");
                txtCitizenId.setStyle("");
                txtInsuranceId.setStyle("");

                selectedUserId = ((Double) tblUsers.getSelectionModel().getSelectedItem().get("id")).longValue();

                lblId.setText("ID: " + ((Double) newValue.get("id")).longValue());
                txtFullName.setText(newValue.get("fullName").toString());
                txtEmail.setText(newValue.get("email").toString());
                cbxSex.getSelectionModel().selectFirst();
                while (!cbxSex.getSelectionModel().getSelectedItem().equals(newValue.get("sex").toString()))
                    cbxSex.getSelectionModel().selectNext();
                dpDob.setValue(LocalDate.parse(newValue.get("dob").toString()));
                txtPhoneNumber.setText(newValue.get("phoneNumber").toString());
                txtAddress.setText(newValue.get("address").toString());
                txtCitizenId.setText(Optional.ofNullable(newValue.get("citizenId")).orElse("").toString());
                txtInsuranceId.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());
            }
        });
    }

    private void clearUserRegistrationInfo() {
        txtFullName2.setText("");
        txtEmail2.setText("");
        txtSex2.setText("");
        txtDob2.setText("");
        txtPhoneNumber2.setText("");
        txtAddress2.setText("");
        txtCitizenId2.setText("");
        txtInsuranceId2.setText("");

        btnApproveRegistration.setVisible(false);
        btnRejectRegistration.setVisible(false);
    }

    void loadUserRegistrationsData() {
        spinner2.setVisible(true);
        clearUserRegistrationInfo();

        String url = AppConstants.BASE_URL + "/users/pending-registrations";
        String token = Utils.getToken();
        String method = "GET";

        userRegistrations = FXCollections.observableArrayList();

        tblUserRegistrations.setItems(userRegistrations);

        tblUserRegistrations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearUserRegistrationInfo();
            } else {
                btnApproveRegistration.setVisible(true);
                btnRejectRegistration.setVisible(true);

                selectedUserRegistrationId = ((Double) tblUserRegistrations.getSelectionModel().getSelectedItem().get("id")).longValue();

                txtFullName2.setText(newValue.get("fullName").toString());
                txtEmail2.setText(newValue.get("email").toString());
                txtSex2.setText(newValue.get("sex").toString());
                txtDob2.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("dob").toString()));
                txtPhoneNumber2.setText(newValue.get("phoneNumber").toString());
                txtAddress2.setText(newValue.get("address").toString());
                txtCitizenId2.setText(Optional.ofNullable(newValue.get("citizenId")).orElse("").toString());
                txtInsuranceId2.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());
            }
        });

        tblUserRegistrations.getColumns().clear();

        TableColumn<Map<String, Object>, String> timeCol = new TableColumn<>("Thời gian");
        timeCol.setCellValueFactory(e -> new SimpleStringProperty(Beans.TIME_FORMAT_CONVERTER.toCustom(e.getValue().get("requested").toString())));

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        TableColumn<Map<String, Object>, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("email").toString()));

        tblUserRegistrations.getColumns().addAll(timeCol, fullNameCol, emailCol);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(url).token(token).method(method)
                                    .build().request();
                            return FXCollections.observableArrayList(result);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                spinner2.setVisible(false);
                userRegistrations.setAll(service.getValue());
            }
        });

        service.start();
    }

    private void clearUserChangeInfo() {
        txtOrgFullName.setText("");
        txtOrgEmail.setText("");
        txtOrgSex.setText("");
        txtOrgDob.setText("");
        txtOrgPhoneNumber.setText("");
        txtOrgAddress.setText("");
        txtOrgCitizenId.setText("");
        txtOrgInsuranceId.setText("");

        txtNewFullName.setText("");
        txtNewEmail.setText("");
        txtNewSex.setText("");
        txtNewDob.setText("");
        txtNewPhoneNumber.setText("");
        txtNewAddress.setText("");
        txtNewCitizenId.setText("");
        txtNewInsuranceId.setText("");

        btnApproveChange.setVisible(false);
        btnRejectChange.setVisible(false);
    }

    void loadUserChangesData() {
        spinner3.setVisible(true);
        clearUserChangeInfo();
        clearChangesBackground();

        userChanges = FXCollections.observableArrayList();

        tblUserChanges.setItems(userChanges);

        tblUserChanges.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearUserChangeInfo();
            } else {
                btnApproveChange.setVisible(true);
                btnRejectChange.setVisible(true);

                selectedUserChangeId = ((Double) tblUserChanges.getSelectionModel().getSelectedItem().get("id")).longValue();

                var original = (Map<String, Object>) newValue.get("original");
                txtOrgFullName.setText(original.get("fullName").toString());
                txtOrgEmail.setText(original.get("email").toString());
                txtOrgSex.setText(original.get("sex").toString());
                txtOrgDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(original.get("dob").toString()));
                txtOrgPhoneNumber.setText(original.get("phoneNumber").toString());
                txtOrgAddress.setText(original.get("address").toString());
                txtOrgCitizenId.setText(Optional.ofNullable(original.get("citizenId")).orElse("").toString());
                txtOrgInsuranceId.setText(Optional.ofNullable(original.get("insuranceId")).orElse("").toString());

                txtNewFullName.setText(newValue.get("fullName").toString());
                txtNewEmail.setText(newValue.get("email").toString());
                txtNewSex.setText(newValue.get("sex").toString());
                txtNewDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("dob").toString()));
                txtNewPhoneNumber.setText(newValue.get("phoneNumber").toString());
                txtNewAddress.setText(newValue.get("address").toString());
                txtNewCitizenId.setText(Optional.ofNullable(newValue.get("citizenId")).orElse("").toString());
                txtNewInsuranceId.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());

                String changeBackground = "-fx-background-color: #ffffe6";
                if (!txtOrgFullName.getText().equals(txtNewFullName.getText())) {
                    txtOrgFullName.setStyle(changeBackground);
                    txtNewFullName.setStyle(changeBackground);
                }
                if (!txtOrgEmail.getText().equals(txtNewEmail.getText())) {
                    txtOrgEmail.setStyle(changeBackground);
                    txtNewEmail.setStyle(changeBackground);
                }
                if (!txtOrgSex.getText().equals(txtNewSex.getText())) {
                    txtOrgSex.setStyle(changeBackground);
                    txtNewSex.setStyle(changeBackground);
                }
                if (!txtOrgDob.getText().equals(txtNewDob.getText())) {
                    txtOrgDob.setStyle(changeBackground);
                    txtNewDob.setStyle(changeBackground);
                }
                if (!txtOrgPhoneNumber.getText().equals(txtNewPhoneNumber.getText())) {
                    txtOrgPhoneNumber.setStyle(changeBackground);
                    txtNewPhoneNumber.setStyle(changeBackground);
                }
                if (!txtOrgAddress.getText().equals(txtNewAddress.getText())) {
                    txtOrgAddress.setStyle(changeBackground);
                    txtNewAddress.setStyle(changeBackground);
                }
                if (!txtOrgCitizenId.getText().equals(txtNewCitizenId.getText())) {
                    txtOrgCitizenId.setStyle(changeBackground);
                    txtNewCitizenId.setStyle(changeBackground);
                }
                if (!txtOrgInsuranceId.getText().equals(txtNewInsuranceId.getText())) {
                    txtOrgInsuranceId.setStyle(changeBackground);
                    txtNewInsuranceId.setStyle(changeBackground);
                }
            }
        });

        tblUserChanges.getColumns().clear();

        TableColumn<Map<String, Object>, String> timeCol = new TableColumn<>("Thời gian");
        timeCol.setCellValueFactory(e -> new SimpleStringProperty(Beans.TIME_FORMAT_CONVERTER.toCustom(e.getValue().get("requested").toString())));

        TableColumn<Map<String, Object>, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("fullName").toString()));

        TableColumn<Map<String, Object>, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("email").toString()));

        tblUserChanges.getColumns().addAll(timeCol, fullNameCol, emailCol);

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(AppConstants.BASE_URL + "/users/pending-changes")
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();

                            result.forEach(map -> {
                                Map<String, Object> original = null;
                                try {
                                    original = new ApiRequest.Builder<List<Map<String, Object>>>()
                                            .url(AppConstants.BASE_URL + "/users/search?q=" + map.get("email").toString())
                                            .token(Utils.getToken())
                                            .method("GET")
                                            .build().request().get(0);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                map.put("original", original);
                            });

                            return FXCollections.observableArrayList(result);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                spinner3.setVisible(false);
                userChanges.setAll(service.getValue());
            }
        });

        service.start();
    }

    @FXML
    void searchUser(KeyEvent event) {
        tblUsers.setItems(users.filtered(e ->
                e.get("email").toString().toLowerCase().contains(txtSearchUser.getText().toLowerCase()) ||
                        e.get("fullName").toString().toLowerCase().contains(txtSearchUser.getText().toLowerCase())));
    }

    @FXML
    void searchUserRegistration(KeyEvent event) {
        tblUserRegistrations.setItems(userRegistrations.filtered(e ->
                e.get("email").toString().toLowerCase().contains(txtSearchUserRegistration.getText().toLowerCase()) ||
                        e.get("fullName").toString().toLowerCase().contains(txtSearchUserRegistration.getText().toLowerCase())));
    }

    @FXML
    void searchUserChange(KeyEvent event) {
        tblUserChanges.setItems(userChanges.filtered(e ->
                e.get("email").toString().toLowerCase().contains(txtSearchUserChange.getText()) ||
                        e.get("fullName").toString().toLowerCase().contains(txtSearchUserChange.getText())));
    }

    @FXML
    void addUser(ActionEvent event) {
        ScreenManager.getAddUserStage(this).show();
    }

    @FXML
    void approveRegistration(ActionEvent event) {
        String url = AppConstants.BASE_URL + "/users/approve-registration?id=" + selectedUserRegistrationId;
        String token = Utils.getToken();
        String method = "POST";
        String requestBody = "";

        try {
            new ApiRequest.Builder<String>()
                    .url(url).token(token).method(method).requestBody(requestBody)
                    .build().request();
        } catch (Exception e) {
            if (e.getMessage().contains("already used")) {
                Utils.showAlert(Alert.AlertType.ERROR, "Email người dùng đã tồn tại trong hệ thống!");
            }
            e.printStackTrace();
            return;
        }

        Utils.showAlert(Alert.AlertType.INFORMATION, "Phê duyệt người dùng thành công!");
        loadUserRegistrationsData();
        loadUsersData();

    }

    @FXML
    void rejectRegistration(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lý do");
        dialog.setHeaderText("Vui lòng nhập lý do từ chối:");
        dialog.setContentText(null);
        Optional<String> reason = dialog.showAndWait();

        while (reason.isEmpty() || reason.get().isBlank()) {
            if (reason.isEmpty()) return;
            Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
            reason = dialog.showAndWait();
        }

        String url = AppConstants.BASE_URL + "/users/reject-registration?id=" + selectedUserRegistrationId + "&reason=" + reason.get();
        System.out.println(url);
        String token = Utils.getToken();
        String method = "POST";
        String requestBody = "";

        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        try {
                            return new ApiRequest.Builder<String>()
                                    .url(url).token(token).method(method).requestBody(requestBody)
                                    .build().request();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(e -> {
            if (service.getValue() != null) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Từ chối đăng ký người dùng thành công!");
                loadUserRegistrationsData();
            }
        });

        service.start();
    }

    private void clearChangesBackground() {
        txtOrgFullName.setStyle("");
        txtOrgEmail.setStyle("");
        txtOrgSex.setStyle("");
        txtOrgDob.setStyle("");
        txtOrgPhoneNumber.setStyle("");
        txtOrgAddress.setStyle("");
        txtOrgCitizenId.setStyle("");
        txtOrgInsuranceId.setStyle("");

        txtNewFullName.setStyle("");
        txtNewEmail.setStyle("");
        txtNewSex.setStyle("");
        txtNewDob.setStyle("");
        txtNewPhoneNumber.setStyle("");
        txtNewAddress.setStyle("");
        txtNewCitizenId.setStyle("");
        txtNewInsuranceId.setStyle("");
    }

    @FXML
    void approveChange(ActionEvent event) {
        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        try {
                            return new ApiRequest.Builder<String>()
                                    .url(AppConstants.BASE_URL + "/users/approve-change?id=" + selectedUserChangeId)
                                    .token(Utils.getToken())
                                    .method("POST")
                                    .build().request();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(e -> {
            if (service.getValue() != null) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Phê duyệt thay đổi thông tin người dùng thành công!");
                clearChangesBackground();
                loadUserChangesData();
                loadUsersData();
            }
        });

        service.start();
    }

    @FXML
    void rejectChange(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lý do");
        dialog.setHeaderText("Vui lòng nhập lý do từ chối:");
        dialog.setContentText(null);
        Optional<String> input = dialog.showAndWait();

        while (input.isEmpty() || input.get().isBlank()) {
            if (input.isEmpty()) return;
            Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
            input = dialog.showAndWait();
        }

        final String reason = input.get();

        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        try {
                            return new ApiRequest.Builder<String>()
                                    .url(AppConstants.BASE_URL + "/users/reject-change?id=" + selectedUserChangeId + "&reason=" + reason)
                                    .token(Utils.getToken())
                                    .method("POST")
                                    .build().request();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(e -> {
            if (service.getValue() != null) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Từ chối thay đổi thông tin người dùng thành công!");
                loadUserChangesData();
            }
        });

        service.start();
    }

    @FXML
    void deleteUser(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", null, "Bạn có chắc muốn xóa người dùng này không?");
        if (!buttonType.equals(ButtonType.OK))
            return;

        String url = AppConstants.BASE_URL + "/users/" + selectedUserId;
        String token = Utils.getToken();
        String method = "DELETE";

        try {
            new ApiRequest.Builder<String>().url(url).token(token).method(method).build().request();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Xóa người dùng thành công!");
            loadUsersData();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateUser(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String url = AppConstants.BASE_URL + "/users/" + selectedUserId;
        String token = Utils.getToken();
        String method = "PUT";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", dpDob.getValue().toString())
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();
        System.out.println(requestBody);
        try {
            new ApiRequest.Builder<String>().url(url).token(token).method(method).requestBody(requestBody).build().request();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Cập nhật hồ sơ người dùng thành công!");
            loadUsersData();
        } catch (Exception e) {
            if (e.getMessage().contains("email_UNIQUE")) {
                lblError.setText("Email đã được sử dụng.");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtEmail.setStyle("");
        txtFullName.setStyle("");
        cbxSex.setStyle("");
        dpDob.setStyle("");
        txtPhoneNumber.setStyle("");
        txtAddress.setStyle("");
        txtCitizenId.setStyle("");
        txtInsuranceId.setStyle("");

        if (txtFullName.getText().isBlank()) {
            txtFullName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền họ tên!");
        }
        if (txtEmail.getText().isBlank()) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập email!");
        }
        if (!txtEmail.getText().matches("[A-Za-z0-9.]+@[A-Za-z0-9]+([.][A-Za-z0-9]+)+")) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Email không hợp lệ!");
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
        if (txtPhoneNumber.getText().isBlank()) {
            txtPhoneNumber.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập số điện thoại!");
        }
        if (!txtPhoneNumber.getText().matches("[0-9]{10,12}")) {
            txtPhoneNumber.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số điện thoại không hợp lệ!");
        }
        if (txtAddress.getText().isBlank()) {
            txtAddress.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền địa chỉ!");
        }
        if (!txtCitizenId.getText().isBlank() && !txtCitizenId.getText().matches("[0-9]{12}")) {
            txtCitizenId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số CCCD không hợp lệ!");
        }
        if (!txtInsuranceId.getText().isBlank() && !txtInsuranceId.getText().matches("[A-Z]{2}[0-9]{13}")) {
            txtInsuranceId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số BHYT khỗng hợp lệ!");
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getUserDetailsPanel(selectedUserId, root, this));
    }

}
