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

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

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
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCitizenId;

    @FXML
    private JFXTextField txtInsuranceId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
    }

    @FXML
    void signUp(ActionEvent event) {
        String url = AppConstants.BASE_URL + "/auth/register";
        String method = "POST";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("password", txtPassword.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", boxSex.getValue())
                .put("dob", LocalDate.parse(txtDob.getText(), Beans.DATE_FORMATTER))
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();
        System.out.println(requestBody);

        try {
            new ApiRequest.Builder<String>()
                    .url(url)
                    .method(method)
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Đăng ký thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Đã có lỗi xảy ra!");
        }
    }

}

