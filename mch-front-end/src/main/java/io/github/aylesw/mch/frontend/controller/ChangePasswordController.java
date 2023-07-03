package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML
    private JFXPasswordField txtOldPassword;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXPasswordField txtConfirm;

    @FXML
    private Label lblError;

    @FXML
    void changePassword(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String requestBody = new RequestBodyMap()
                .put("oldPassword", txtOldPassword.getText())
                .put("newPassword", txtNewPassword.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/users/" + UserIdentity.getUserId() + "/change-password")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            ((Stage) txtOldPassword.getScene().getWindow()).close();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            if (e.getMessage().contains("Old password does not match")) {
                lblError.setText("Mật khẩu cũ không đúng!");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtOldPassword.setStyle("");
        txtNewPassword.setStyle("");
        txtConfirm.setStyle("");

        if (txtOldPassword.getText().isBlank()) {
            txtOldPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập mật khẩu cũ!");
        }
        if (txtNewPassword.getText().isBlank()) {
            txtNewPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập mật khẩu mới!");
        }
        if (txtNewPassword.getText().length() < 8) {
            txtNewPassword.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Mật khẩu mới cần có 8 ký tự trở lên!");
        }
        if (txtConfirm.getText().isBlank()) {
            txtConfirm.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập lại mật khẩu!");
        }
        if (!txtConfirm.getText().equals(txtNewPassword.getText())) {
            txtConfirm.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Mật khẩu xác nhận không khớp!");
        }
    }

}
