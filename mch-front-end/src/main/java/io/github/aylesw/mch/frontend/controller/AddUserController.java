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
    void addUser(ActionEvent event) {
        String url = AppConstants.BASE_URL + "/users";
        String token = Utils.getToken();
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

        try {
            new ApiRequest.Builder<String>()
                    .url(url)
                    .token(token)
                    .method(method)
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Thêm người dùng thành công!");
            ((Stage)txtEmail.getScene().getWindow()).close();
            parent.loadUsersData();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Đã có lỗi xảy ra!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage)txtEmail.getScene().getWindow()).close();
    }

}
