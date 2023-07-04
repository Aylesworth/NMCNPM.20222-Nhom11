package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    private ManageUsersController parent;

    public AddUserController(ManageUsersController parent) {
        this.parent = parent;
    }

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXComboBox<String> boxSex;

    @FXML
    private DatePicker dpDob;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCitizenId;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);
    }

    @FXML
    void addUser(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String url = AppConstants.BASE_URL + "/users";
        String token = Utils.getToken();
        String method = "POST";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("password", txtPassword.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", boxSex.getValue())
                .put("dob", dpDob.getValue().toString())
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(url)
                    .token(token)
                    .method(method)
                    .requestBody(requestBody)
                    .build().request();

//            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Thêm người dùng thành công!");
            ((Stage) txtEmail.getScene().getWindow()).close();
            parent.loadUsersData();
        } catch (Exception e) {
            if (e.getMessage().contains("Email already used")) {
                lblError.setText("Email đã được sử dụng!");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtEmail.setStyle("");
        txtPassword.setStyle("");
        txtConfirmPassword.setStyle("");
        txtFullName.setStyle("");
        boxSex.setStyle("");
        dpDob.setStyle("");
        txtPhoneNumber.setStyle("");
        txtAddress.setStyle("");
        txtCitizenId.setStyle("");
        txtInsuranceId.setStyle("");

        if (txtEmail.getText().isBlank()) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập email!");
        }
        if (!txtEmail.getText().matches("[A-Za-z0-9.]+@[A-Za-z0-9]+([.][A-Za-z0-9]+)+")) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Email không hợp lệ!");
        }
        if (txtPassword.getText().isBlank()) {
            txtPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập mật khẩu!");
        }
        if (txtPassword.getText().length() < 8) {
            txtPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Mật khẩu cần có 8 ký tự trở lên!");
        }
        if (txtConfirmPassword.getText().isBlank()) {
            txtConfirmPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập lại mật khẩu!");
        }
        if (!txtConfirmPassword.getText().equals(txtPassword.getText())) {
            txtConfirmPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Mật khẩu xác nhận không khớp!");
        }
        if (txtFullName.getText().isBlank()) {
            txtFullName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền họ tên!");
        }
        if (boxSex.getSelectionModel().getSelectedItem() == null) {
            boxSex.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn giới tính!");
        }
        LocalDate dob;
        try {
            dob = LocalDate.parse(dpDob.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDob.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày sinh không hợp lệ!");
        }
        if (dob.isAfter(LocalDate.now())) {
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
    void cancel(ActionEvent event) {
        ((Stage) txtEmail.getScene().getWindow()).close();
    }

}
