package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.RequestBodyMap;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML
    private VBox firstForm;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Label lblError1;

    @FXML
    private VBox secondForm;

    @FXML
    private JFXTextField txtAuthCode;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXPasswordField txtConfirm;

    @FXML
    private Label lblError2;

    private String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstForm.setVisible(true);
        secondForm.setVisible(false);
    }

    @FXML
    void resetPassword(ActionEvent event) {
        lblError2.setText("");
        txtAuthCode.setStyle("");
        txtNewPassword.setStyle("");
        txtConfirm.setStyle("");

        if (txtAuthCode.getText().isBlank()) {
            txtAuthCode.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError2.setText("Vui lòng nhập mã xác minh!");
            return;
        }
        if (txtNewPassword.getText().isBlank()) {
            txtNewPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError2.setText("Vui lòng nhập mật khẩu mới!");
            return;
        }
        if (txtNewPassword.getText().length()<8) {
            txtNewPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError2.setText("Mật khẩu mới cần có 8 ký tự trở lên!");
            return;
        }
        if (txtConfirm.getText().isBlank()) {
            txtConfirm.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError2.setText("Vui lòng nhập lại mật khẩu!");
            return;
        }
        if (!txtConfirm.getText().equals(txtNewPassword.getText())) {
            txtConfirm.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError2.setText("Mật khẩu xác nhận không khớp!");
            return;
        }

        try {
            String requestBody = new RequestBodyMap()
                    .put("userEmail", email)
                    .put("newPassword", txtNewPassword.getText())
                    .put("authCode", txtAuthCode.getText())
                    .toJson();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL+"/auth/reset-password")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            ((Stage) firstForm.getScene().getWindow()).close();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Mật khẩu của bạn đã được đặt lại thành công!");
        } catch (Exception e) {
            if (e.getMessage().contains("invalid or expired")) {
                lblError2.setText("Mã xác minh không hợp lệ hoặc đã hết hạn.");
            }
        }
    }

    @FXML
    void sendCode(ActionEvent event) {
        lblError1.setText("");
        txtEmail.setStyle("");

        if (txtEmail.getText().isBlank()) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError1.setText("Vui lòng nhập email!");
            return;
        }
        if (!txtEmail.getText().matches("[A-Za-z0-9.]+@[A-Za-z0-9]+([.][A-Za-z0-9]+)+")) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            lblError1.setText("Email không hợp lệ!");
            return;
        }

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/auth/request-password-reset")
                    .method("POST")
                    .requestBody(txtEmail.getText())
                    .build().request();

            email = txtEmail.getText();

            firstForm.setVisible(false);
            secondForm.setVisible(true);
        } catch (Exception e) {
            if (e.getMessage().contains("doesn't exist")) {
                lblError1.setText("Không tồn tại người dùng với email này!");
            }
        }
    }

}
