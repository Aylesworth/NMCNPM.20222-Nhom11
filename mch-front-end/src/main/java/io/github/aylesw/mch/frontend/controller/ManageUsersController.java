package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
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
    private JFXComboBox<String> cbxSex2;

    @FXML
    private JFXButton btnDeclineRegistration;

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
    private ProgressIndicator spinner;

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

        Service<ObservableList<Map<String,Object>>> service = new Service<ObservableList<Map<String, Object>>>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<ObservableList<Map<String, Object>>>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        var result = new ApiRequest.Builder<List<Map<String,Object>>>()
                                .url(url).token(token).method(method)
                                .build().request();
                        result.removeIf(e -> e.get("email").toString().equals("admin"));
                        return FXCollections.observableArrayList(result);
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            users.setAll(service.getValue());
            spinner.setVisible(false);
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

                lblId.setText("ID: " + ((Double) newValue.get("id")).longValue());
                txtFullName.setText(newValue.get("fullName").toString());
                txtEmail.setText(newValue.get("email").toString());
                cbxSex.getSelectionModel().selectFirst();
                while (!cbxSex.getSelectionModel().getSelectedItem().equals(newValue.get("sex").toString()))
                    cbxSex.getSelectionModel().selectNext();
                txtDob.setText(Utils.convertDateFormat(newValue.get("dob").toString()));
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
        cbxSex2.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        txtDob2.setText("");
        txtPhoneNumber2.setText("");
        txtAddress2.setText("");
        txtCitizenId2.setText("");
        txtInsuranceId2.setText("");

        btnApproveRegistration.setVisible(false);
        btnDeclineRegistration.setVisible(false);
    }

    void loadUserRegistrationsData() {
        String url = AppConstants.BASE_URL + "/users/pending-registrations";
        String token = Utils.getToken();
        String method = "GET";

        List<Map<String, Object>> result;
        try {
            result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(url).token(token).method(method)
                    .build().request();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        userRegistrations = FXCollections.observableArrayList(result);

        tblUserRegistrations.setItems(userRegistrations);

        tblUserRegistrations.getColumns().clear();

        TableColumn<Map<String, Object>, String> timeCol = new TableColumn<>("Thời gian");
        timeCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get("requested").toString()));

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
                btnDeclineRegistration.setVisible(true);

                txtFullName2.setText(newValue.get("fullName").toString());
                txtEmail2.setText(newValue.get("email").toString());
                cbxSex2.getSelectionModel().selectFirst();
                while (!cbxSex2.getSelectionModel().getSelectedItem().equals(newValue.get("sex").toString()))
                    cbxSex2.getSelectionModel().selectNext();
                txtDob2.setText(Utils.localDateToString(Utils.stringToLocalDateISO(newValue.get("dob").toString())));
                txtPhoneNumber2.setText(newValue.get("phoneNumber").toString());
                txtAddress2.setText(newValue.get("address").toString());
                txtCitizenId2.setText(Optional.ofNullable(newValue.get("citizenId")).orElse("").toString());
                txtInsuranceId2.setText(Optional.ofNullable(newValue.get("insuranceId")).orElse("").toString());
            }
        });
    }

    @FXML
    void searchUser(KeyEvent event) {
        tblUsers.setItems(users.filtered(e ->
                e.get("email").toString().contains(txtSearchUser.getText()) ||
                        e.get("fullName").toString().contains(txtSearchUser.getText())));
    }

    @FXML
    void addUser(ActionEvent event) {
        ScreenManager.addUserStage().show();
    }

    @FXML
    void approveRegistration(ActionEvent event) {

    }

    @FXML
    void declineRegistration(ActionEvent event) {

    }

    @FXML
    void deleteUser(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", null, "Bạn có chắc muốn xóa người dùng này không?");
        if (!buttonType.equals(ButtonType.OK))
            return;

        long id = ((Double) tblUsers.getSelectionModel().getSelectedItem().get("id")).longValue();
        String url = AppConstants.BASE_URL + "/users/" + id;
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
        long id = ((Double) tblUsers.getSelectionModel().getSelectedItem().get("id")).longValue();
        String url = AppConstants.BASE_URL + "/users/" + id;
        String token = Utils.getToken();
        String method = "PUT";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", Utils.stringToLocalDateCustom(txtDob.getText()))
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();
        System.out.println(requestBody);
        try {
            new ApiRequest.Builder<String>().url(url).token(token).method(method).requestBody(requestBody).build().request();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Cập nhật người dùng thành công!");
            loadUsersData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {

    }

}
