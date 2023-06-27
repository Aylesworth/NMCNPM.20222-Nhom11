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
    private JFXTextField txtDob;

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
    private JFXComboBox<String> cbxOrgSex;

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
    private JFXComboBox<String> cbxNewSex;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private ProgressIndicator spinner2;

    private long selectedUserId;
    private long selectedUserRegistrationId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUsersData();
        loadUserRegistrationsData();
    }

    private void clearUserInfo() {
        lblId.setText("ID: ");
        txtFullName.setText("");
        txtEmail.setText("");
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        cbxSex.getSelectionModel().clearSelection();
        txtDob.setText("");
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

                selectedUserId = ((Double) tblUsers.getSelectionModel().getSelectedItem().get("id")).longValue();

                lblId.setText("ID: " + ((Double) newValue.get("id")).longValue());
                txtFullName.setText(newValue.get("fullName").toString());
                txtEmail.setText(newValue.get("email").toString());
                cbxSex.getSelectionModel().selectFirst();
                while (!cbxSex.getSelectionModel().getSelectedItem().equals(newValue.get("sex").toString()))
                    cbxSex.getSelectionModel().selectNext();
                txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("dob").toString()));
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

        clearUserRegistrationInfo();

        tblUserRegistrations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearUserRegistrationInfo();
            } else {
                btnApproveRegistration.setVisible(true);
                btnRejectRegistration.setVisible(true);

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

    @FXML
    void searchUser(KeyEvent event) {
        tblUsers.setItems(users.filtered(e ->
                e.get("email").toString().contains(txtSearchUser.getText()) ||
                        e.get("fullName").toString().contains(txtSearchUser.getText())));
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
                Utils.showAlert(Alert.AlertType.INFORMATION, "Phê duyệt người dùng thành công!");
                loadUserRegistrationsData();
            }
        });

        service.start();
    }

    @FXML
    void rejectRegistration(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lý do");
        dialog.setHeaderText("Vui lòng nhập lý do từ chối:");
        dialog.setContentText(null);
        Optional<String> reason = dialog.showAndWait();

        if (!reason.isPresent()) {
            //
            return;
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
        String url = AppConstants.BASE_URL + "/users/" + selectedUserId;
        String token = Utils.getToken();
        String method = "PUT";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", Beans.DATE_FORMAT_CONVERTER.toISO(txtDob.getText()))
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
            throw new RuntimeException(e);
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getUserDetailsPanel(selectedUserId, root, this));
    }

}
